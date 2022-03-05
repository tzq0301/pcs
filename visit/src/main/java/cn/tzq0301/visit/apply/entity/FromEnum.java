package cn.tzq0301.visit.apply.entity;

/**
 * @author tzq0301
 * @version 1.0
 */
public enum FromEnum {
    NINE(9), TEN(10), ELEVEN(11), FOURTEEN(14), FIFTEEN(15), SIXTEEN(16);

    private final Integer from;

    FromEnum(Integer from) {
        this.from = from;
    }

    public Integer getFrom() {
        return from;
    }
}
