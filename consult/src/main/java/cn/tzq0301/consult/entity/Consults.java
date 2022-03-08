package cn.tzq0301.consult.entity;

import cn.tzq0301.consult.entity.visit.VisitRecord;
import com.google.common.collect.Lists;

import java.time.LocalDate;
import java.util.List;

/**
 * @author tzq0301
 * @version 1.0
 */
public final class Consults {
    // FIXME
    public static Consult newConsult(final VisitRecord visitRecord, int weekday, int from, String address) {
        List<Record> records = Lists.newArrayList();
        // FIXME

        return new Consult(visitRecord.getId(), visitRecord.getStudentId(), visitRecord.getStudentName(),
                visitRecord.getStudentSex(), visitRecord.getStudentBirthday(), visitRecord.getStudentPhone(),
                visitRecord.getStudentEmail(), visitRecord.getStudentIdentity(), visitRecord.getVisitorId(),
                visitRecord.getVisitorName(), visitRecord.getVisitorSex(), visitRecord.getVisitorPhone(),
                visitRecord.getVisitorEmail(), visitRecord.getProblemId(), visitRecord.getProblemDetail(),
                visitRecord.getDay(), visitRecord.getFrom(), visitRecord.getAddress(), visitRecord.getScores(),
                visitRecord.getSumScore(), visitRecord.getScaleResult(), visitRecord.getApplyPassTime(),
                visitRecord.getStatus(), visitRecord.getDangerLevel(), null, null, null, null, null, 0,
                ConsultStatusEnum.IN_PROGRESS.getCode(), new Pattern(weekday, from, address), records, LocalDate.now());
    }

    private Consults() {}
}
