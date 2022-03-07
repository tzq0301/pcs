package cn.tzq0301.visit.record.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author tzq0301
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponsibleVisitRecord {
    private String globalId;

    private String studentId;

    private String studentName;

    private Integer studentSex;

    private String studentPhone;

    private String day;

    private Integer from;

    private String address;

    private Integer status;
}
