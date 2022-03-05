package cn.tzq0301.visit.apply.entity;

/**
 * @author tzq0301
 * @version 1.0
 */
public enum ApplyStatusEnum {
    PENDING_REVIEW(0, "待审核"),
    PASS(1, "已通过"),
    REJECT(2, "被拒绝"),
    REVOKE(3, "已撤销");

    private final Integer code;

    private final String name;

    ApplyStatusEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
