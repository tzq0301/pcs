package cn.tzq0301.consult.entity;

import cn.tzq0301.consult.entity.visit.VisitRecord;
import com.google.common.collect.Lists;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.List;

/**
 * @author tzq0301
 * @version 1.0
 */
public final class Consults {
    public static Consult newConsult(final String id, final VisitRecord visitRecord, final UserInfo consultor,
                                     final int weekday, final int from, final String address, List<Record> records) {
        return new Consult(new ObjectId(id), visitRecord.getStudentId(), visitRecord.getStudentName(),
                visitRecord.getStudentSex(), visitRecord.getStudentBirthday(), visitRecord.getStudentPhone(),
                visitRecord.getStudentEmail(), visitRecord.getStudentIdentity(), visitRecord.getVisitorId(),
                visitRecord.getVisitorName(), visitRecord.getVisitorSex(), visitRecord.getVisitorPhone(),
                visitRecord.getVisitorEmail(), visitRecord.getProblemId(), visitRecord.getProblemDetail(),
                visitRecord.getDay(), visitRecord.getFrom(), visitRecord.getAddress(), visitRecord.getScores(),
                visitRecord.getSumScore(), visitRecord.getScaleResult(), visitRecord.getApplyPassTime(),
                visitRecord.getStatus(), visitRecord.getDangerLevel(), consultor.getUserId(),
                consultor.getName(), consultor.getSex(), consultor.getPhone(), consultor.getEmail(), 0,
                ConsultStatusEnum.IN_PROGRESS.getCode(), new Pattern(weekday, from, address), records,
                null, null, LocalDate.now());
    }

    private Consults() {}
}
