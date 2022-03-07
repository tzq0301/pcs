package cn.tzq0301.duty.entity.work;

/**
 * @author tzq0301
 * @version 1.0
 */
public final class Works {
    public static Work newWork(final String userId) {
        return new Work(userId);
    }

    private Works() {}
}
