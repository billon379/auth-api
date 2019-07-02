package fun.billon.auth.api.model;

import lombok.Data;

import java.util.Date;

/**
 * 内部服务密钥
 *
 * @author billon
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
public class AuthOuterKeyModel {

    /**
     * 平台(1):android
     */
    public static final int PLATFORM_ANDROID = 1;
    /**
     * 平台(1):iOS
     */
    public static final int PLATFORM_IOS = 2;
    /**
     * 平台(3):H5
     */
    public static final int PLATFORM_H5 = 3;

    /**
     * 外部应用id
     */
    private String appId;

    /**
     * 平台(1:android;2:iOS;3:H5)
     */
    private Integer platform;

    /**
     * 外部应用名称
     */
    private String name;

    /**
     * 给外部应用分配的密钥
     */
    private String appSecret;

    /**
     * jwt生成的token过期时间,单位秒(s)
     */
    private Integer tokenExpTime;

    /**
     * jwt刷新token的过期时间,单位秒(s)
     */
    private Integer refreshTokenExpTime;

    /**
     * 记录创建时间
     */
    private Date createTime;

    public AuthOuterKeyModel() {
    }

    /**
     * 构造方法
     *
     * @param appId               外部应用id
     * @param platform            平台(1:android;2:iOS;3:H5)
     * @param name                外部应用名称
     * @param appSecret           给外部应用分配的密钥
     * @param tokenExpTime        jwt生成的token过期时间,单位秒(s)
     * @param refreshTokenExpTime jwt刷新token的过期时间,单位秒(s)
     */
    public AuthOuterKeyModel(String appId, Integer platform, String name,
                             String appSecret, Integer tokenExpTime, Integer refreshTokenExpTime) {
        this.appId = appId;
        this.platform = platform;
        this.name = name;
        this.appSecret = appSecret;
        this.tokenExpTime = tokenExpTime;
        this.refreshTokenExpTime = refreshTokenExpTime;
    }

}