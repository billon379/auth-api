package fun.billon.auth.api.constant;

/**
 * 缓存相关常量配置
 *
 * @author billon
 * @version 1.0.0
 * @since 1.0.0
 */
public class AuthCacheConstant {

    /**
     * AuthInnerKeyModel内部服务密钥相关缓存配置
     */
    /**
     * AuthInnerKeyModel 内部服务密钥相关缓存配置
     */
    public static final String CACHE_NAMESPACE_AUTH_INNTER_KEY_MODEL = "AuthInnerKeyModel";
    /**
     * 缓存键值sid
     */
    public static final String CACHE_KEY_AUTH_INNER_KEY_MODEL_SID = "#{authInnerKeyModel.sid}";
    /**
     * 缓存键值sid
     */
    public static final String CACHE_KEY_AUTH_INNER_KEY_SID = "#{sid}";


    /**
     * AuthInnerRuleIpModel内部服务ip过滤规则相关缓存配置
     */
    /**
     * AuthInnerRuleIpModel 内部服务ip过滤规则相关缓存配置
     */
    public static final String CACHE_NAMESPACE_AUTH_INNTER_RULE_IP_MODEL = "AuthInnerRuleIpModel";
    /**
     * 缓存键值sid
     */
    public static final String CACHE_KEY_AUTH_INNER_RULE_IP_MODEL_SID = "#{authInnerRuleIpModel.sid}";
    /**
     * 缓存键值sid
     */
    public static final String CACHE_KEY_AUTH_INNER_RULE_IP_SID = "#{sid}";


    /**
     * AuthOuterKeyModel外部服务密钥相关缓存配置
     */
    /**
     * AuthOuterKeyModel 外部服务密钥相关缓存配置
     */
    public static final String CACHE_NAMESPACE_AUTH_OUTER_KEY_MODEL = "AuthOuterKeyModel";
    /**
     * 缓存键值appId
     */
    public static final String CACHE_KEY_AUTH_OUTER_KEY_MODEL_APPID = "#{authOuterKeyModel.appId}";
    /**
     * 缓存键值appId
     */
    public static final String CACHE_KEY_AUTH_OUTER_KEY_APPID = "#{appId}";

}