package cn.tzq0301.util;

import java.time.LocalDate;

/**
 * @author tzq0301
 * @version 1.0
 */
public final class DateUtils {
    public static String localDateToString(final LocalDate localDate) {
        int year = localDate.getYear();
        int month = localDate.getMonthValue();
        int day = localDate.getDayOfMonth();

        return String.valueOf(year)
                + (isPositiveAndLessThanTen(month) ? "0" + month : month)
                + (isPositiveAndLessThanTen(day) ? "0" + day : day);
    }

    private static boolean isPositiveAndLessThanTen(int number) {
        return number >= 0 && number < 10;
    }

    public DateUtils() {}
}
