package cn.tzq0301.gateway.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author tzq0301
 * @version 1.0
 */
public final class PhoneUtils {
    private static final Pattern PHONE_PATTERN = Pattern.compile("^((13[0-9])|(14[0|5|6|7|9])|(15[0-3])|(15[5-9])|(16[6|7])|(17[2|3|5|6|7|8])|(18[0-9])|(19[1|8|9]))\\d{8}$");

    public static boolean isValid(final String phone) {
        Matcher matcher = PHONE_PATTERN.matcher(phone);
        return matcher.find();
    }

    private PhoneUtils() {}
}
