package cn.tzq0301.auth.user.entity;

import cn.tzq0301.result.ResultEnumerable;

/**
 * @author tzq0301
 * @version 1.0
 */
public enum UserResultEnum implements ResultEnumerable {
    SUCCESS(0, "请求成功"),
    USER_ID_NOT_MATCH(1, "用户 ID 不匹配");

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
