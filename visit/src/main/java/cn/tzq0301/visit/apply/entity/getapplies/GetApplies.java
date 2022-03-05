package cn.tzq0301.visit.apply.entity.getapplies;

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
public class GetApplies {
    private String recordId;

    private Integer status;

    private String applyTime;

    private String day;

    private Integer from;

    private String address;

    private String problemId;
}
