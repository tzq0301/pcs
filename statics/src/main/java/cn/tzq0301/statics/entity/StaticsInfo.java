package cn.tzq0301.statics.entity;

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
public class StaticsInfo {
    private String globalId;

    private String studentName;

    private String studentPhone;

    private String visitorName;

    private String visitorPhone;

    private String consultorName;

    private String consultorPhone;

    private Integer problemId;

    private String problemDetail;
}
