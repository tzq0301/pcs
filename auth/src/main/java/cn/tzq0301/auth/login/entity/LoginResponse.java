package cn.tzq0301.auth.login.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author tzq0301
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    /**
     * {@link cn.tzq0301.auth.entity.user.User} 中的 userId（学工号）
     */
    private String id;

    /**
     * {@link cn.tzq0301.auth.entity.user.User} 中的 name
     */
    private String username;

    /**
     * 角色
     */
    private String role;
}
