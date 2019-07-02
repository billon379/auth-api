package fun.billon.auth.api.hystrix;

import fun.billon.auth.api.feign.IAuthService;
import fun.billon.auth.api.model.AuthInnerKeyModel;
import fun.billon.auth.api.model.AuthInnerRuleIpModel;
import fun.billon.auth.api.model.AuthOuterKeyModel;
import fun.billon.common.constant.CommonStatusCode;
import fun.billon.common.model.ResultModel;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 授权服务(AUTH)hystrix断路器
 *
 * @author billon
 * @version 1.0.0
 * @since 1.0.0
 */
@Component
public class AuthServiceImplHystrix implements IAuthService {

    /**
     * 获取token
     *
     * @param authentication 请求头中的鉴权信息 必填
     * @param sid            请求头中的内部服务id 必填
     * @param appId          外部应用id 必填
     * @param uid            要签名的用户id 必填
     * @param paramMap       token中要存放的附加信息 选填
     * @return 使用appId签发的token
     */
    @Override
    @GetMapping("/token/{appId}/{uid}")
    public ResultModel<String> token(@RequestHeader(value = "authentication") String authentication,
                                     @RequestHeader(value = "sid") String sid,
                                     @PathVariable(value = "appId") String appId,
                                     @PathVariable(value = "uid") String uid,
                                     @RequestParam Map<String, String> paramMap) {
        ResultModel resultModel = new ResultModel();
        resultModel.setFailed(CommonStatusCode.HYSTRIX_FALLBACK, "AUTH:HYSTRIX:FALLBACK:GET /token/" + appId + "/" + uid
                + " -H [Authentication:" + authentication + ",sid:" + sid + "]" + "-D [paramMap:" + paramMap + "]");
        return resultModel;
    }

    /**
     * 新增外部应用密钥
     *
     * @param authentication 请求头中的鉴权信息 必填
     * @param sid            请求头中的内部服务id 必填
     * @param paramMap       paramMap.platform            平台(1:android;2:iOS;3:H5) 必填
     *                       paramMap.name                外部应用名称 必填
     *                       paramMap.tokenExpTime        jwt生成的token过期时间,单位秒(s) 必填
     *                       paramMap.refreshTokenExpTime jwt刷新token的过期时间,单位秒(s) 必填
     * @return 外部应用密钥
     */
    @Override
    @PostMapping(value = "/outer/key")
    public ResultModel<AuthOuterKeyModel> addOuterKey(@RequestHeader(value = "authentication") String authentication,
                                                      @RequestHeader(value = "sid") String sid,
                                                      @RequestParam Map<String, String> paramMap) {
        ResultModel resultModel = new ResultModel();
        resultModel.setFailed(CommonStatusCode.HYSTRIX_FALLBACK, "AUTH:HYSTRIX:FALLBACK:POST /outer/key"
                + " -H [authentication:" + authentication + ",sid:" + sid + "]" + "-D [paramMap:" + paramMap + "]");
        return resultModel;
    }

    /**
     * 获取外部应用密钥
     *
     * @param authentication 请求头中的鉴权信息 必填
     * @param sid            请求头中的内部服务id 必填
     * @param appId          外部应用id 必填
     * @return 获取外部应用密钥
     */
    @Override
    @GetMapping("/outer/key/{appId}")
    public ResultModel<AuthOuterKeyModel> outerKey(@RequestHeader(value = "authentication") String authentication,
                                                   @RequestHeader(value = "sid") String sid,
                                                   @PathVariable(value = "appId") String appId) {
        ResultModel resultModel = new ResultModel();
        resultModel.setFailed(CommonStatusCode.HYSTRIX_FALLBACK, "AUTH:HYSTRIX:FALLBACK:GET /outer/key/" + appId
                + " -H [authentication:" + authentication + ",sid:" + sid + "]");
        return resultModel;
    }

    /**
     * 获取内部服务密钥
     *
     * @param authentication 请求头中的鉴权信息 必填
     * @param sid            请求头中的内部服务id 必填
     * @param paramMap       paramMap.name 服务名称 必填
     *                       paramMap.desc 描述 必填
     * @return 内部服务密钥
     */
    @Override
    @PostMapping(value = "/inner/key")
    public ResultModel<AuthInnerKeyModel> addInnerKey(@RequestHeader(value = "authentication") String authentication,
                                                      @RequestHeader(value = "sid") String sid,
                                                      @RequestParam Map<String, String> paramMap) {
        ResultModel resultModel = new ResultModel();
        resultModel.setFailed(CommonStatusCode.HYSTRIX_FALLBACK, "AUTH:HYSTRIX:FALLBACK:POST /inner/key"
                + " -H [authentication:" + authentication + ",sid:" + sid + "]" + "-D [paramMap:" + paramMap + "]");
        return resultModel;
    }

    /**
     * 获取内部服务密钥
     *
     * @param authentication 请求头中的鉴权信息 必填
     * @param sid            请求头中的内部服务id 必填
     * @return 内部服务密钥
     */
    @Override
    @GetMapping("/inner/key/{sid}")
    public ResultModel<AuthInnerKeyModel> innerKey(@RequestHeader(value = "authentication") String authentication,
                                                   @RequestHeader(value = "sid") String sid) {
        ResultModel resultModel = new ResultModel();
        resultModel.setFailed(CommonStatusCode.HYSTRIX_FALLBACK, "AUTH:HYSTRIX:FALLBACK:GET /inner/key/" + sid
                + " -H [authentication:" + authentication + ",sid:" + sid + "]");
        return resultModel;
    }

    /**
     * 获取内部服务ip过滤规则
     *
     * @param sid 请求头中的内部服务id 必填
     * @return 内部服务过滤规则
     */
    @Override
    @GetMapping("/inner/rule/ip/{sid}")
    public ResultModel<AuthInnerRuleIpModel> innerIpRule(@RequestHeader(value = "sid") String sid) {
        ResultModel resultModel = new ResultModel();
        resultModel.setFailed(CommonStatusCode.HYSTRIX_FALLBACK, "AUTH:HYSTRIX:FALLBACK:GET /inner/rule/ip/" + sid
                + " -H [sid:" + sid + "]");
        return resultModel;
    }

}