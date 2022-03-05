package cn.tzq0301.visit.apply.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.List;

/**
 * @author tzq0301
 * @version 1.0
 */
@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Apply {
    @Id
    @Field("_id")
    private String id;

    private String userId; // 学工号

    private String phone; // 电话号码

    private String email; // 邮箱

    private Integer problemId; // 问题类型

    private String problemDetail; // 问题详述

    private LocalDate day; // 具体日期

    private Integer from; // 开始时间（小时）

    private String address; // 初访地点

    private List<Integer> scores; // 分数列表（共 84 个分数，最后 4 个分数需要乘 20

    private Integer sumScore; // 将 scores 换算成百分数（分数 / 总分），例如 0.94

    private String scaleResult; // 量表的结果

    private Integer order; // 根据 sumScore 计算出 order

    private LocalDate applyPassTime; // 初访审核通过时间

    private Integer status; // 申请状态

    private LocalDate createdTime; // 创建时间

    Apply(String userId, String phone, String email, Integer problemId, String problemDetail,
                 LocalDate day, Integer from, String address, List<Integer> scores, Integer sumScore,
                 String scaleResult, Integer order, LocalDate applyPassTime, Integer status, LocalDate createdTime) {
        this.userId = userId;
        this.phone = phone;
        this.email = email;
        this.problemId = problemId;
        this.problemDetail = problemDetail;
        this.day = day;
        this.from = from;
        this.address = address;
        this.scores = scores;
        this.sumScore = sumScore;
        this.scaleResult = scaleResult;
        this.order = order;
        this.applyPassTime = applyPassTime;
        this.status = status;
        this.createdTime = createdTime;
    }
}
