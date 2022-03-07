package cn.tzq0301.duty.entity.duty;

/**
 * @author tzq0301
 * @version 1.0
 */
public final class Duties {
    public static Duty newDuty(final String userId) {
        return new Duty(userId);
    }

    private Duties() {}
}
