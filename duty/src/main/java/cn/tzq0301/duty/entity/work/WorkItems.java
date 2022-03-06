package cn.tzq0301.duty.entity.work;

import cn.tzq0301.util.DateUtils;

import java.time.LocalDate;

/**
 * @author tzq0301
 * @version 1.0
 */
public final class WorkItems {
    public static WorkItem newWorkItem(LocalDate day, Integer from, String address) {
        return new WorkItem(day, from, address);
    }

    public static WorkItem newWorkItem(String day, Integer from, String address) {
        return new WorkItem(DateUtils.stringToLocalDate(day), from, address);
    }

    private WorkItems() {}
}
