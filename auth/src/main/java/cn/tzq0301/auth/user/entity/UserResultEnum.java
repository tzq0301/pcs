package cn.tzq0301.auth.user.entity;

import cn.tzq0301.result.ResultEnumerable;

/**
 * @author tzq0301
 * @version 1.0
 */
public enum UserResultEnum implements ResultEnumerable {
    SUCCESS(0, "请求成功"),
    USER_ID_NOT_MATCH(1, "用户 ID 不匹配"),
    OLD_PASSWORD_NOT_CORRECT(2, "旧密码错误"),
    UPDATE_FAIL(3, "修改失败"),
    PASSWORD_DUPLICATE(4, "新密码与旧密码重复");

    private final Integer code;

    private final String message;

    UserResultEnum(Integer code, String message) {
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
