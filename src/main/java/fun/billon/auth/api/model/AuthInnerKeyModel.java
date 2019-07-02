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
public class AuthInnerKeyModel {

    /**
     * 内部服务id
     */
    private String sid;

    /**
     * 服务名称
     */
    private String name;

    /**
     * 描述
     */
    private String desc;

    /**
     * 分配给内部服务的密钥
     */
    private String secret;

    /**
     * 记录创建时间
     */
    private Date createTime;

    public AuthInnerKeyModel() {
    }

    /**
     * 构造方法
     *
     * @param sid    内部服务id
     * @param name   服务名称
     * @param desc   描述
     * @param secret 分配给内部服务的密钥
     */
    public AuthInnerKeyModel(String sid, String name, String desc, String secret) {
        this.sid = sid;
        this.name = name;
        this.desc = desc;
        this.secret = secret;
    }

}