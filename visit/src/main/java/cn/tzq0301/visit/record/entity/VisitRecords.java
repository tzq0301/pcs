package cn.tzq0301.visit.record.entity;

import cn.tzq0301.util.DateUtils;
import cn.tzq0301.visit.apply.entity.Apply;
import cn.tzq0301.visit.apply.entity.UserInfo;

import java.time.LocalDate;

/**
 * @author tzq0301
 * @version 1.0
 */
public final class VisitRecords {
    public static VisitRecord newVisitRecord(Apply apply, UserInfo visitor, Integer problemId, String problemDetail,
                                      String day, Integer from, String address) {
        return new VisitRecord(apply.getId(), apply.getUserId(), apply.getName(), apply.getSex(), apply.getBirthday(),
                apply.getPhone(), apply.getEmail(), apply.getIdentity(), visitor.getUserId(), visitor.getName(),
                visitor.getSex(), visitor.getPhone(), visitor.getEmail(), problemId, problemDetail, DateUtils.stringToLocalDate(day),
                from, address, apply.getScores(), apply.getSumScore(), apply.getScaleResult(), LocalDate.now(),
                0, apply.getOrder(), null, null, LocalDate.now());
    }

    private VisitRecords() {
    }
}
