package cn.tzq0301.duty.entity.duty;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author tzq0301
 * @version 1.0
 */
public final class Patterns {
    public static Pattern newPattern(Integer weekday, Integer from, String address) {
        return new Pattern(weekday, from, address);
    }

    private Patterns() {}
}
