package cn.tzq0301.duty.entity.duty;

import cn.tzq0301.util.DateUtils;

import java.time.LocalDate;

/**
 * @author tzq0301
 * @version 1.0
 */
public final class SpecialItems {
    public static SpecialItem newSpecialItem(LocalDate date, Integer from, String address, Integer type) {
        return new SpecialItem(date, from, address, type);
    }

    public static SpecialItem newSpecialItem(String date, Integer from, String address, Integer type) {
        return new SpecialItem(DateUtils.stringToLocalDate(date), from, address, type);
    }

    private SpecialItems() {}
}
