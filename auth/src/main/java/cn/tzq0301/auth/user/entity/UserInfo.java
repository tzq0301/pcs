package cn.tzq0301.auth.user.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * @author tzq0301
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
    private String userId; // 学工号

    private String name; // 姓名

    private String role; // 角色

    private Integer sex; // 性别

    private LocalDate birthday; // 出生日期

    private String phone; // 手机号码

    private String email; // 邮箱

    private String identity; // 身份证号

    private Integer studentStatus; // 不在进行为 0、正在初访为 1、正在心理咨询为 2
}
