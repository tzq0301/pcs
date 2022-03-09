package cn.tzq0301.auth.user.entity.vo;

import java.io.Serializable;

/**
 * @author tzq0301
 * @version 1.0
 */
public class UserInfoResponse implements Serializable {

    private static final long serialVersionUID = -4206067226896311203L;

    private String name;

    private String role;

    private String sex;

    private String birthday;

    private String phone;

    private String email;

    private String identity;

    public UserInfoResponse(String name, String role, String sex, String birthday, String phone, String email, String identity) {
        this.name = name;
        this.role = role;
        this.sex = sex;
        this.birthday = birthday;
        this.phone = phone;
        this.email = email;
        this.identity = identity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }
}
