package fun.billon.auth.api.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import fun.billon.auth.api.feign.IAuthService;
import fun.billon.auth.api.model.AuthOuterKeyModel;
import fun.billon.common.constant.CommonStatusCode;
import fun.billon.common.encrypt.MD5;
import fun.billon.common.model.ResultModel;
import fun.billon.common.util.HttpResponseUtils;
import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * 签名验证，对于某些敏感接口，需要对客户端请求进行签名验证
 * 签名验证规则：
 * 1.将url路径+请求参数值的字母排序拼接成字串s
 * 2.使用appId对于的secret对s进行md5编码
 * 3.验证编码后的字串跟请求头中的signature是否一致
 *
 * @author billon
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
public class SignatureInterceptor extends AbstractAuthInterceptor {

    /**
     * 内部服务id(sid)
     */
    private String sid;

    /**
     * 授权服务
     */
    private IAuthService authService;

    /**
     * 设置要检查的参数
     *
     * @return Map  要检查的参数属性名称-->要检查的参数属性值
     */
    @Override
    protected Map<String, Object> getCheckParams() {
        Map<String, Object> map = new HashMap<>(2);
        map.put("sid", sid);
        map.put("authService", authService);
        return map;
    }

    /**
     * 设置要检查的请求头
     *
     * @return Map 要检查的请求头-->请求头不存在时返回的错误码
     */
    @Override
    protected Map<String, Integer> getCheckHeaders() {
        Map<String, Integer> map = new HashMap<>(2);
        map.put("appId", CommonStatusCode.APPID_INVALID);
        map.put("signature", CommonStatusCode.SIGNATURE_INVALID);
        return map;
    }

    /**
     * 数据处理
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @return
     * @throws Exception
     */
    @Override
    protected boolean handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ResultModel resultModel = new ResultModel();
        AuthOuterKeyModel authOuterKeyModel;
        ResultModel<AuthOuterKeyModel> resultAuthOuterKeyModel = authService.outerKey(sid, sid, request.getHeader("appId"));
        if (resultAuthOuterKeyModel.getCode() == ResultModel.RESULT_SUCCESS
                && resultAuthOuterKeyModel.getData() != null) {
            // 通过鉴权服务获取到数据
            authOuterKeyModel = resultAuthOuterKeyModel.getData();
        } else {
            // 访问授权服务失败
            resultModel.setFailed(CommonStatusCode.SID_INVALID, "sid[" + sid + "]无效");
            HttpResponseUtils.write(response, JSONObject.toJSONString(resultModel));
            return false;
        }

        /*
         * 获取到外部应用密钥，对signature进行验签
         */
        String path = request.getRequestURI();
        Map<String, String[]> map = new TreeMap<>();
        map.putAll(request.getParameterMap());
        StringBuilder sb = new StringBuilder(authOuterKeyModel.getAppSecret() + path);
        for (String key : map.keySet()) {
            sb.append(map.get(key)[0]);
        }
        if (request.getHeader("signature").equals(MD5.encode(sb.toString()))) {
            // 验签成功
            return true;
        }

        /*
         * 验签失败
         */
        resultModel.setFailed(CommonStatusCode.SIGNATURE_INVALID, "signature[" + request.getHeader("signature") + "]验签失败");
        HttpResponseUtils.write(response, JSON.toJSONString(resultModel));
        return false;
    }

}