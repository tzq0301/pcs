package cn.tzq0301.util;

import com.google.common.base.Strings;

import java.time.LocalDate;
import java.util.Objects;

/**
 * @author tzq0301
 * @version 1.0
 */
public final class DateUtils {
    private static final int YEAR_LENGTH = 4;

    private static final int MONTH_LENGTH = 2;

    private static final int DAY_LENGTH = 2;

    private static final int DATE_LENGTH = YEAR_LENGTH + MONTH_LENGTH + DAY_LENGTH;

    public static String localDateToString(final LocalDate localDate) {
        if (Objects.isNull(localDate)) {
            throw new IllegalArgumentException();
        }

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

    public static LocalDate stringToLocalDate(final String str) {
        if (Strings.isNullOrEmpty(str) || str.length() != DATE_LENGTH) {
            throw new IllegalArgumentException();
        }

        int year = Integer.parseInt(str.substring(0, YEAR_LENGTH));
        int month = Integer.parseInt(str.substring(YEAR_LENGTH, YEAR_LENGTH + MONTH_LENGTH));
        int day = Integer.parseInt(str.substring(YEAR_LENGTH + MONTH_LENGTH, DATE_LENGTH));

        return LocalDate.of(year, month, day);
    }

    public DateUtils() {}
}
