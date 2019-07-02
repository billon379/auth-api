package fun.billon.auth.api.feign;

import fun.billon.auth.api.constant.AuthCacheConstant;
import fun.billon.auth.api.hystrix.AuthServiceImplHystrix;
import fun.billon.auth.api.model.AuthInnerKeyModel;
import fun.billon.auth.api.model.AuthInnerRuleIpModel;
import fun.billon.auth.api.model.AuthOuterKeyModel;
import fun.billon.common.cache.CacheType;
import fun.billon.common.cache.annotation.Cacheable;
import fun.billon.common.cache.interceptor.ResultCacheInterceptor;
import fun.billon.common.model.ResultModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 授权服务(AUTH)接口
 * 1.服务提供方在Eureka中注册，服务名称为AUTH
 * 2.使用Feign声明Http客户端
 * 3.使用hystrix实现断路器,实现类为AuthServiceImplHystrix
 *
 * @author billon
 * @version 1.0.0
 * @since 1.0.0
 */
@FeignClient(value = "auth", fallback = AuthServiceImplHystrix.class)
public interface IAuthService {

    /**
     * 获取token
     *
     * @param authentication 请求头中的鉴权信息 必填
     * @param sid            请求头中的内部服务id 必填
     * @param appId          外部应用id 必填
     * @param uid            要签名的用户id 必填
     * @param paramMap       token中要存放的附加信息 选填
     * @return 使用jwt签发的token
     */
    @GetMapping("/token/{appId}/{uid}")
    ResultModel<String> token(@RequestHeader(value = "authentication") String authentication,
                              @RequestHeader(value = "sid") String sid,
                              @PathVariable(value = "appId") String appId,
                              @PathVariable(value = "uid") String uid,
                              @RequestParam Map<String, String> paramMap);

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
    @PostMapping(value = "/outer/key")
    ResultModel<AuthOuterKeyModel> addOuterKey(@RequestHeader(value = "authentication") String authentication,
                                               @RequestHeader(value = "sid") String sid,
                                               @RequestParam Map<String, String> paramMap);

    /**
     * 获取外部应用密钥
     *
     * @param authentication 请求头中的鉴权信息 必填
     * @param sid            请求头中的内部服务id 必填
     * @param appId          外部应用id 必填
     * @return 获取外部应用密钥
     */
    @GetMapping("/outer/key/{appId}")
    @Cacheable(namespace = AuthCacheConstant.CACHE_NAMESPACE_AUTH_OUTER_KEY_MODEL,
            key = AuthCacheConstant.CACHE_KEY_AUTH_OUTER_KEY_APPID,
            type = AuthOuterKeyModel.class, cacheType = CacheType.HASH,
            expire = 60 * 60 * 24 * 30, cacheInterceptor = ResultCacheInterceptor.class)
    ResultModel<AuthOuterKeyModel> outerKey(@RequestHeader(value = "authentication") String authentication,
                                            @RequestHeader(value = "sid") String sid,
                                            @PathVariable(value = "appId") String appId);

    /**
     * 获取内部服务密钥
     *
     * @param authentication 请求头中的鉴权信息 必填
     * @param sid            请求头中的内部服务id 必填
     * @param paramMap       paramMap.name 服务名称 必填
     *                       paramMap.desc 描述 必填
     * @return 内部服务密钥
     */
    @PostMapping(value = "/inner/key")
    ResultModel<AuthInnerKeyModel> addInnerKey(@RequestHeader(value = "authentication") String authentication,
                                               @RequestHeader(value = "sid") String sid,
                                               @RequestParam Map<String, String> paramMap);

    /**
     * 获取内部服务密钥
     *
     * @param authentication 请求头中的鉴权信息 必填
     * @param sid            请求头中的内部服务id 必填
     * @return 内部服务密钥
     */
    @GetMapping("/inner/key/{sid}")
    @Cacheable(namespace = AuthCacheConstant.CACHE_NAMESPACE_AUTH_INNTER_KEY_MODEL,
            key = AuthCacheConstant.CACHE_KEY_AUTH_INNER_KEY_SID,
            type = AuthInnerKeyModel.class, cacheType = CacheType.HASH,
            expire = 60 * 60 * 24 * 30, cacheInterceptor = ResultCacheInterceptor.class)
    ResultModel<AuthInnerKeyModel> innerKey(@RequestHeader(value = "authentication") String authentication,
                                            @RequestHeader(value = "sid") String sid);

    /**
     * 获取内部服务ip过滤规则
     *
     * @param sid 请求头中的内部服务id 必填
     * @return 内部服务过滤规则
     */
    @GetMapping("/inner/rule/ip/{sid}")
    @Cacheable(namespace = AuthCacheConstant.CACHE_NAMESPACE_AUTH_INNTER_RULE_IP_MODEL,
            key = AuthCacheConstant.CACHE_KEY_AUTH_INNER_RULE_IP_SID,
            type = AuthInnerRuleIpModel.class, cacheType = CacheType.HASH,
            expire = 60 * 60 * 24 * 30, cacheInterceptor = ResultCacheInterceptor.class)
    ResultModel<AuthInnerRuleIpModel> innerIpRule(@RequestHeader(value = "sid") String sid);

}