package cn.tzq0301.gateway.login.entity;

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
     * userId（学工号）
     */
    private String id;

    /**
     * 学生姓名（name）
     */
//    private String username;

    /**
     * 角色
     */
    private String role;
}
