package cn.tzq0301.gateway.login.entity;

import cn.tzq0301.result.ResultEnumerable;

/**
 * @author tzq0301
 * @version 1.0
 */
public enum LoginResponseCode implements ResultEnumerable {
    SUCCESS(0, "登录成功"),
    ERROR(1, "登录失败");

    private final Integer code;

    private final String message;

    LoginResponseCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
