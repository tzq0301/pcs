package cn.tzq0301.util;

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

    private CommonUtils() {}
}
