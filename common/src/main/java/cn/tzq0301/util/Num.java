package cn.tzq0301.util;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author tzq0301
 * @version 1.0
 */
public final class Num {
    public static final Integer ZERO = 0;

    public static final Integer ONE = 1;

    public static final Integer TWO = 2;

    public static final Integer THREE = 3;

    public static final Integer FOUR = 4;

    public static final Integer FIVE = 5;

    private static final List<String> chineseNumbers = Lists.newArrayList("零", "一", "二", "三", "四", "五", "六", "七");

    public static String getChinese(final int num) {
        return chineseNumbers.get(num);
    }

    private Num() {}
}
