package cn.tzq0301.visit.apply.entity;

/**
 * @author tzq0301
 * @version 1.0
 */
public enum ProblemEnum {
    DEVELOP(0, "发展性心理问题"),
    ADAPT(1, "适应性心理健康问题"),
    OBSTACLE(2, "障碍性心理健康问题"),
    OTHER(3, "其他");

    private final Integer code;

    private final String name;

    ProblemEnum(Integer code, String name) {
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
