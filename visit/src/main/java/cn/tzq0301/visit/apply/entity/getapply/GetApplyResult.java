package cn.tzq0301.visit.apply.entity.getapply;

import cn.tzq0301.result.ResultEnumerable;

/**
 * @author tzq0301
 * @version 1.0
 */
public enum GetApplyResult implements ResultEnumerable {
    SUCCESS(0, "请求成功"),
    APPLY_NOT_FOUNT(1, "没有该初访申请记录");

    private final Integer code;

    private final String message;

    GetApplyResult(Integer code, String message) {
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
