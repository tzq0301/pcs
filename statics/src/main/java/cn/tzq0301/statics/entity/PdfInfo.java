package cn.tzq0301.statics.entity;

import cn.tzq0301.util.DateUtils;
import cn.tzq0301.util.SexUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * @author tzq0301
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PdfInfo {
    private static final long serialVersionUID = 1407861343043549909L;

    private PatientInfo patientInfo;

    private DoctorInfo doctorInfo;

    private String selfCommon;

    private String summary;

    private List<ConsultRecord> consultRecords;

    private String date; // 结案时间

    public PdfInfo(String studentName, Integer studentSex, String studentPhone, String studentEmail,
                   LocalDate studentBirthday, String consultorName, Integer consultorSex, String consultorPhone,
                   String consultorEmail, String selfComment, String detail, List<ConsultRecord> consultRecords,
                   LocalDate createdTime) {
        this.patientInfo = new PatientInfo(studentName, SexUtils.sexOfString(studentSex), studentPhone,
                studentEmail, DateUtils.formatToChineseDateString(studentBirthday));
        this.doctorInfo = new DoctorInfo(consultorName, SexUtils.sexOfString(consultorSex),
                consultorPhone, consultorEmail);
        this.selfCommon = selfComment;
        this.summary = detail;
        this.date = DateUtils.formatToChineseDateString(createdTime);
        this.consultRecords = consultRecords;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class PatientInfo {
        private String name;

        private String gender;

        private String phone;

        private String email;

        private String birthday;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class DoctorInfo {
        private String name;

        private String gender;

        private String phone;

        private String email;
    }
}
