package cn.tzq0301.visit.apply.entity.unfinished;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.List;

/**
 * @author tzq0301
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnfinishedApply {
    private String globalId; // Global ID

    private String studentId; // 学生学工号

    private String studentName; // 学生姓名

    private Integer studentSex; // 学生性别

    private String studentPhone; // 学生电话号码

    private String studentEmail; // 学生邮箱

    private Integer problemId; // 问题类型

    private String problemDetail; // 问题详述

    private Integer order; // 根据 sumScore 计算出 order

    private String day; // 具体日期

    private Integer from; // 开始时间（小时）

    private String address; // 初访地点

    private String visitorId; // 预选初访员 ID

    private String visitorName; // 预选初访员姓名
}
