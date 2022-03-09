package cn.tzq0301.util;

import org.slf4j.Logger;

import java.util.Optional;

/**
 * @author tzq0301
 * @version 1.0
 */
public final class CommonUtils {
    @SuppressWarnings("unchecked")
    public static <T> Optional<T> castToOptional(Object obj) {
        return (Optional<T>) obj;
    }

    public static void printlnBefore(final Logger log, final String str) {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>\tBefore {}\t>>>>>>>>>>>>>>>>>>>>>>>>>", str);
    }

    public static void printlnAfter(final Logger log, final String str) {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>\tAfter  {}\t>>>>>>>>>>>>>>>>>>>>>>>>>", str);
    }

    private CommonUtils() {}

}
