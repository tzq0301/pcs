package cn.tzq0301.consult.entity;

/**
 * @author tzq0301
 * @version 1.0
 */
public enum ConsultStatusEnum {
    IN_PROGRESS(0, "正在进行"),
    NOT_TOTALLY_FINISHED(1, "已完成但未填总结"),
    FINISHED(2, "已完成且已填总结");

    private final Integer code;

    private final String message;

    ConsultStatusEnum(Integer code, String message) {
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
