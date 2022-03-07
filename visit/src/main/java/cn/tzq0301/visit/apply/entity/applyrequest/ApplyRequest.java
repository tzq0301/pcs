package cn.tzq0301.visit.apply.entity.applyrequest;

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
@AllArgsConstructor
public class ApplyRequest {
    private String id; // 学工号

    private String phone; // 电话号码

    private String email; // 邮箱

    private Integer problemId; // 问题类型

    private String problemDetail; // 问题详述

    private String day; // 具体日期

    private Integer from; // 开始时间（小时）

    private String address; // 初访地点

    private List<Integer> scores; // 分数列表（共 84 个分数，最后 4 个分数需要乘 20

    private String visitorId; // 预选初访员
}
