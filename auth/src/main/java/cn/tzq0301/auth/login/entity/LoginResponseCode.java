package cn.tzq0301.auth.login.entity;

/**
 * @author tzq0301
 * @version 1.0
 */
public enum LoginResponseCode {
    SUCCESS(0, "登录成功"),
    ERROR(1, "登录失败");

    private final Integer code;

    private final String message;

    LoginResponseCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
