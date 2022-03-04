package cn.tzq0301.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author tzq0301
 * @version 1.0
 */
class DateUtilsTest {

    @Test
    void localDateToString() {
        assertEquals("20010301", DateUtils.localDateToString(LocalDate.of(2001, 3, 1)));
        assertEquals("20011201", DateUtils.localDateToString(LocalDate.of(2001, 12, 1)));
        assertEquals("20011211", DateUtils.localDateToString(LocalDate.of(2001, 12, 11)));
    }
}