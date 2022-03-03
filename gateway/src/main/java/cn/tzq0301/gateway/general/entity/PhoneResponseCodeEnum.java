package cn.tzq0301.gateway.general.entity;

import cn.tzq0301.result.ResultEnumerable;

/**
 * @author tzq0301
 * @version 1.0
 */
public enum PhoneResponseCodeEnum implements ResultEnumerable {
    SUCCESS(0, "验证码发送成功"),
    PHONE_NOT_FOUNT(1, "没有该手机号"),
    PHONE_FORMAT_ERROR(2, "手机号格式错误"),
    MESSAGE_SEND_ERROR(3, "验证码发送失败");

    private final Integer code;

    private final String message;

    PhoneResponseCodeEnum(Integer code, String message) {
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

    @Override
    public String toString() {
        return "PhoneResponseCodeEnum{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
