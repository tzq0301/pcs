package cn.tzq0301.auth.entity.user;

/**
 * 用户角色
 *
 * @author tzq0301
 * @version 1.0
 */
public enum Role {
    ADMIN("ADMIN"), // 中央管理员
    STUDENT("STUDENT"), // 学生
    VISITOR("VISITOR"), // 初访员
    ASSISTANT("ASSISTANT"), // 心理助理
    CONSULTANT("CONSULTANT"); // 咨询师

    private final String role;

    Role(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
