package cn.tzq0301.auth.entity.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author tzq0301
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class User {
    @Id
    @Field("_id")
    private String id;

    @Indexed(unique = true)
    private String userId; // 学工号

    private String name; // 姓名

    private String password; // 密码

    private Boolean enable; // 该账号是否有效

    private Role role; // 角色

    private Sex sex; // 性别

    private LocalDate birthday; // 出生日期

    @Indexed(unique = true)
    private String phone; // 手机号码

    @Indexed(unique = true)
    private String email; // 邮箱

    @Indexed(unique = true)
    private String identity; // 身份证号

    private Integer studentStatus; // 不在进行为 0、正在初访为 1、正在心理咨询为 2

    public User(String userId, String name, String password, Boolean enable,
                Role role, Sex sex, LocalDate birthday,
                String phone, String email, String identity, Integer studentStatus) {
        this.userId = userId;
        this.name = name;
        this.password = password;
        this.enable = enable;
        this.role = role;
        this.sex = sex;
        this.birthday = birthday;
        this.phone = phone;
        this.email = email;
        this.identity = identity;
        this.studentStatus = studentStatus;
    }

    public Integer getStudentStatus() {
        if (!Role.STUDENT.equals(this.role)) {
            return null;
        }

        return studentStatus;
    }

    public void setStudentStatus(Integer studentStatus) {
        if (Role.STUDENT.equals(this.role)) {
            this.studentStatus = studentStatus;
        }
    }
}
