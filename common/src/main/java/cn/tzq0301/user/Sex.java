package cn.tzq0301.user;

/**
 * @author tzq0301
 * @version 1.0
 */
public enum Sex {
    FEMALE(0), // 女
    MALE(1); // 男

    private final Integer sex;

    Sex(Integer sex) {
        this.sex = sex;
    }

    public Integer getSex() {
        return sex;
    }
}
