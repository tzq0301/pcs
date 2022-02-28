package cn.tzq0301.auth.entity.user;

import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

/**
 * @author tzq0301
 * @version 1.0
 */
@Document
public class User {

    private String userId; // 学工号

    private String name; // 姓名

    private String password; // 密码

    private Boolean enable; // 该账号是否有效

    private Role role; // 角色

    private Sex sex; // 性别

    private LocalDate birthday; // 出生日期

    private String phone; // 手机号码

    private String email; // 邮箱

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

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public Boolean getEnable() {
        return enable;
    }

    public Role getRole() {
        return role;
    }

    public Sex getSex() {
        return sex;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getIdentity() {
        return identity;
    }

    public Integer getStudentStatus() {
        if (!Role.STUDENT.equals(this.role)) {
            return null;
        }

        return studentStatus;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public void setStudentStatus(Integer studentStatus) {
        if (Role.STUDENT.equals(this.role)) {
            this.studentStatus = studentStatus;
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", enable=" + enable +
                ", role=" + role +
                ", sex=" + sex +
                ", birthday=" + birthday +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", identity='" + identity + '\'' +
                ", studentStatus=" + studentStatus +
                '}';
    }
}
