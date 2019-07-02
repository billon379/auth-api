package fun.billon.auth.api.interceptor;

import com.alibaba.fastjson.JSONObject;
import fun.billon.auth.api.feign.IAuthService;
import fun.billon.auth.api.model.AuthInnerKeyModel;
import fun.billon.common.constant.CommonStatusCode;
import fun.billon.common.model.ResultModel;
import fun.billon.common.util.HttpResponseUtils;
import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 内部服务身份验证。
 * 该拦截器通过对sid和authentication做校验来验证请求来自内部服务
 *
 * @author billon
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
public class InnerKeyInterceptor extends AbstractAuthInterceptor {

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
        Map<String, Object> map = new HashMap<>(1);
        map.put("authService", authService);
        return map;
    }

    /**
     * 设置要检查的请求头
     *
     * @return Map  要检查的请求头-->请求头不存在时返回的错误码
     */
    @Override
    protected Map<String, Integer> getCheckHeaders() {
        Map<String, Integer> map = new HashMap<>(2);
        map.put("sid", CommonStatusCode.SID_INVALID);
        map.put("authentication", CommonStatusCode.AUTHENTICATION_INVALID);
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
        ResultModel<AuthInnerKeyModel> resultModelAuthInnerKey = authService.innerKey(request.getHeader("authentication"),
                request.getHeader("sid"));
        if (resultModelAuthInnerKey.getCode() == ResultModel.RESULT_SUCCESS
                && resultModelAuthInnerKey.getData() != null) {
            /**
             * TODO,对authentication进行校验
             */
            return true;
        } else {
            // 访问授权服务失败
            resultModel.setFailed(CommonStatusCode.SID_INVALID, "sid[" + request.getHeader("sid") + "]无效");
            HttpResponseUtils.write(response, JSONObject.toJSONString(resultModel));
            return false;
        }
    }

}