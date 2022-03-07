package cn.tzq0301.visit.apply.entity.passapply;

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
public class PassApplyRequest {
    private String globalId;

    private String visitorId;

    private Integer problemId;

    private String problemDetail;

    private String day;

    private Integer from;
}
