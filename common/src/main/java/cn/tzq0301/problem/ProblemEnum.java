package cn.tzq0301.problem;

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

    public static String getName(Integer code) {
        switch (code) {
            case 0:
                return DEVELOP.getName();
            case 1:
                return ADAPT.getName();
            case 2:
                return OBSTACLE.getName();
            case 3:
                return OTHER.getName();
            default:
                throw new IllegalArgumentException();
        }
    }
}
