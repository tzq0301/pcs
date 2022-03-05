package cn.tzq0301.visit.apply.entity.vo;

import cn.tzq0301.result.ResultEnumerable;

/**
 * @author tzq0301
 * @version 1.0
 */
public enum ApplyRequestResult implements ResultEnumerable {
    NO_NEED_TO_APPLY_TWICE(0, "无需重复发起申请"),
    PHONE_FORMAT_INCORRECT(1, "手机号格式不正确");

    private final Integer code;

    private final String message;

    ApplyRequestResult(Integer code, String message) {
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
