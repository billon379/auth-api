package fun.billon.auth.api.model;

import lombok.Data;

import java.util.Date;

/**
 * 內部服务过滤规则，ip地址过滤
 *
 * @author billon
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
public class AuthInnerRuleIpModel {

    /**
     * 内部服务分配的id
     */
    private String sid;

    /**
     * 允许访问该模块的ip,多个ip之间使用","号分隔
     */
    private String ip;

    /**
     * 记录创建时间
     */
    private Date createTime;

}