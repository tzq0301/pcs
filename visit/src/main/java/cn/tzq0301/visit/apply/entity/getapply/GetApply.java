package cn.tzq0301.visit.apply.entity.getapply;

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
public class GetApply {
    private String phone; // 电话号码

    private String email; // 邮箱

    private String problemId; // 问题类型

    private String problemDetail; // 问题详述

    private String day; // 具体日期

    private Integer from; // 开始时间（小时）

    private String address; // 初访地点
}
