package cn.tzq0301.user;

/**
 * @author tzq0301
 * @version 1.0
 */
public enum Sex {
    FEMALE(0, "女"), // 女
    MALE(1, "男"); // 男

    private final Integer sex;

    private final String str;

    Sex(Integer sex, String str) {
        this.sex = sex;
        this.str = str;
    }

    public Integer getSex() {
        return sex;
    }

    public String getStr() {
        return str;
    }

    public static Sex from(Integer sex) {
        if (new Integer(0).equals(sex)) {
            return FEMALE;
        } else if (new Integer(1).equals(sex)) {
            return MALE;
        } else {
            return null;
        }
    }


}
