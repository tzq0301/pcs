package cn.tzq0301.util;

import cn.tzq0301.user.Sex;

import java.util.Objects;

/**
 * @author tzq0301
 * @version 1.0
 */
public class SexUtils {
    public static String sexOfString(Integer sex) {
        return Objects.requireNonNull(Sex.from(sex)).getStr();
    }

    public static String sexOfString(Sex sex) {
        return sex.getStr();
    }
}
