package cn.tzq0301.visit.record.entity.vo;

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
public class UnHandledConsultApply {
    private String globalId; // Global ID

    private String studentId; // 学生 ID

    private String studentName; // 学生姓名

    private String studentSex; // 学生性别

    private String studentPhone; // 学生电话号码

    private Integer problemId; // 问题类型

    private String problemDetail; // 问题详述

    private String day; // 初访具体日期

    private String scaleResult; // 量表的结果

    private Integer dangerLevel; // 初访员给出的危险级别

    private Integer result; // 初访结论（无需咨询为 0、安排咨询为 1、转介送诊为 2）

    private Integer consultApplyStatus; // 申请状态（申请中为 0、心理助理已安排咨询为 1）

    private String visitorName;
}
