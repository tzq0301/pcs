package cn.tzq0301.statics.entity;

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
public class UserInfo {
    private String id; // 学工号

    private String name; // 姓名

    private String role; // 角色

    private String sex; // 性别

    private String birthday; // 出生日期

    private String phone; // 手机号码

    private String email; // 邮箱

    private String identity; // 身份证号
}
