package fun.billon.auth.api.interceptor;

import com.alibaba.fastjson.JSONObject;
import fun.billon.auth.api.feign.IAuthService;
import fun.billon.auth.api.model.AuthInnerRuleIpModel;
import fun.billon.common.constant.CommonStatusCode;
import fun.billon.common.model.ResultModel;
import fun.billon.common.util.HttpRequestUtils;
import fun.billon.common.util.HttpResponseUtils;
import fun.billon.common.util.StringUtils;
import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 內部服务ip地址过滤
 *
 * @author billon
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
public class InnerRuleIpInterceptor extends AbstractAuthInterceptor {

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
        AuthInnerRuleIpModel authInnerRuleIpModel;
        ResultModel<AuthInnerRuleIpModel> resultModelAuthInnerRuleIpModel = authService.innerIpRule(sid);
        if (resultModelAuthInnerRuleIpModel.getCode() == ResultModel.RESULT_SUCCESS
                && resultModelAuthInnerRuleIpModel.getData() != null) {
            // 通过鉴权服务获取到数据，鉴权通过
            authInnerRuleIpModel = resultModelAuthInnerRuleIpModel.getData();
        } else {
            // 访问授权服务失败
            resultModel.setFailed(CommonStatusCode.SID_INVALID, "sid[" + sid + "]无效");
            HttpResponseUtils.write(response, JSONObject.toJSONString(resultModel));
            return false;
        }

        /*
         * 获取到ip过滤规则，且当前ip与过滤规则匹配，则验证通过
         */
        String ip = HttpRequestUtils.getIpAddress(request);
        if (StringUtils.matches(authInnerRuleIpModel.getIp(), ip)) {
            return true;
        }

        resultModel.setFailed(CommonStatusCode.IP_INVALID, "当前ip[" + ip + "]无访问权限");
        HttpResponseUtils.write(response, JSONObject.toJSONString(resultModel));
        return false;
    }

}
