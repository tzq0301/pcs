package cn.tzq0301.consult.entity.student;

import cn.tzq0301.consult.entity.Record;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author tzq0301
 * @version 1.0
 */
@Data
@NoArgsConstructor
public class StudentConsultDetail {
    private List<Record> records;

    private Integer dangerLevel;

    private String problemId;

    private String problemDetail;

    private String consultorName;

    private String consultorSex;

    private String consultorPhone;

    private String consultorEmail;

    private Integer times;

    public StudentConsultDetail(List<Record> records, Integer dangerLevel, String problemId,
                                String problemDetail, String consultorName, String consultorSex,
                                String consultorPhone, String consultorEmail, Integer times) {
        this.records = records;
        this.dangerLevel = dangerLevel;
        this.problemId = problemId;
        this.problemDetail = problemDetail;
        this.consultorName = consultorName;
        this.consultorSex = consultorSex;
        this.consultorPhone = consultorPhone;
        this.consultorEmail = consultorEmail;
        this.times = times;
    }
}
