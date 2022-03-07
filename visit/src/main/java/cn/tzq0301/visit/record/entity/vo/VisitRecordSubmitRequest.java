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
public class VisitRecordSubmitRequest {
    private Integer dangerLevel;

    private Integer result;

    private Integer problemId;

    private String problemDetail;
}
