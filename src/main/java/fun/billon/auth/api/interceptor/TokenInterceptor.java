package fun.billon.auth.api.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import fun.billon.auth.api.feign.IAuthService;
import fun.billon.auth.api.model.AuthOuterKeyModel;
import fun.billon.common.constant.CommonStatusCode;
import fun.billon.common.model.ResultModel;
import fun.billon.common.util.HttpResponseUtils;
import fun.billon.common.util.JwtUtils;
import fun.billon.common.util.StringUtils;
import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * token验证
 *
 * @author billon
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
public class TokenInterceptor extends AbstractAuthInterceptor {

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
     * @return Map 要检查的参数属性名称-->要检查的参数属性值
     */
    @Override
    protected Map<String, Object> getCheckParams() {
        Map<String, Object> map = new HashMap<>(2);
        map.put("authService", authService);
        map.put("sid", sid);
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
        map.put("token", CommonStatusCode.TOKEN_INVALID);
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
        ResultModel<AuthOuterKeyModel> resultModelAuthJwtKeyModel = authService.outerKey(sid, sid, request.getHeader("appId"));
        if (resultModelAuthJwtKeyModel.getCode() == ResultModel.RESULT_SUCCESS
                && resultModelAuthJwtKeyModel.getData() != null) {
            // 通过鉴权服务获取到数据
            authOuterKeyModel = resultModelAuthJwtKeyModel.getData();
        } else {
            // 访问授权服务失败
            resultModel.setFailed(CommonStatusCode.APPID_INVALID, "appId[" + request.getHeader("appId") + "]无效");
            HttpResponseUtils.write(response, JSONObject.toJSONString(resultModel));
            return false;
        }

        /*
         * 获取到外部应用密钥，对token进行验证
         */
        try {
            DecodedJWT jwt = JwtUtils.verify(authOuterKeyModel.getAppId(), authOuterKeyModel.getAppSecret(),
                    authOuterKeyModel.getRefreshTokenExpTime(), request.getHeader("token"));
            // 将uid设置到request中
            request.setAttribute("uid", jwt.getSubject());

            // 将token中的附加信息(claim:ext)设置到request中
            String extras = jwt.getClaim("ext").asString();
            if (!StringUtils.isEmpty(extras)) {
                JSONObject jsonObject = JSONObject.parseObject(extras);
                for (String key : jsonObject.keySet()) {
                    request.setAttribute(key, jsonObject.getString(key));
                }
            }

            // token过期但是未失效，可以通过token获取新的签名
            if (jwt.getExpiresAt().getTime() < System.currentTimeMillis()) {
                request.setAttribute("token_status", CommonStatusCode.TOKEN_EXPIRED);
            }
            return true;
        } catch (JWTVerificationException e) {
            e.printStackTrace();
        }

        resultModel.setFailed(CommonStatusCode.TOKEN_INVALID, "token[" + request.getHeader("token") + "]已失效，请重新申请token");
        HttpResponseUtils.write(response, JSON.toJSONString(resultModel));
        return false;
    }

}