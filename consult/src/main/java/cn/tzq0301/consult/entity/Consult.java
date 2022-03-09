package cn.tzq0301.consult.entity;

import cn.tzq0301.util.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static cn.tzq0301.util.Num.*;

/**
 * @author tzq0301
 * @version 1.0
 */
@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Consult implements Serializable {

    private static final long serialVersionUID = 1316023038426580037L;

    private static final int REGULAR_TIMES = 8;

    @Id
    @Field("_id")
    @MongoId(FieldType.OBJECT_ID)
    private ObjectId id; // Global ID

    private String studentId; // 学生 ID

    private String studentName; // 学生姓名

    private Integer studentSex; // 学生性别

    private LocalDate studentBirthday; // 学生生日

    private String studentPhone; // 学生电话号码

    private String studentEmail; // 学生邮箱

    private String studentIdentity; // 学生身份证

    private String visitorId; // 初访员 ID

    private String visitorName; // 初访员姓名

    private Integer visitorSex; // 初访员性别

    private String visitorPhone; // 初访员电话号码

    private String visitorEmail; // 初访员邮箱

    private Integer problemId; // 问题类型

    private String problemDetail; // 问题详述

    private LocalDate day; // 初访具体日期

    private Integer from; // 初访开始时间（小时）

    private String address; // 初访地点

    private List<Integer> scores; // 分数列表（共 84 个分数，最后 4 个分数需要乘 20

    private Integer sumScore; // 将 scores 换算成百分数（分数 / 总分），例如 0.94

    private String scaleResult; // 量表的结果

    private LocalDate applyPassTime; // 初访审核通过时间

    private Integer status; // 状态（未完成为 0，已完成为 1）

    private Integer dangerLevel; // 初访员给出的危险级别

    private String consultorId; // 咨询师 ID

    private String consultorName; // 咨询师姓名

    private Integer consultorSex; // 咨询师性别

    private String consultorPhone; // 咨询师电话

    private String consultorEmail; // 咨询师邮箱

    private Integer times; // 已进行过的咨询次数

    private Integer consultStatus; // 咨询状态（正在进行为 0、已完成但未填总结为 1、已完成且已填总结为 2）

    private Pattern pattern;

    private List<Record> records;

    private String selfComment;

    private String detail;

    private LocalDate createdTime; // 创建时间

    public List<Record> addRecord(final Record record) {
        List<Record> noUse = null;

        records.set(times, record);
        times++;

        if (THREE.equals(record.getStatus())) { // FINISHED
            consultStatus = ONE;
            if (times < REGULAR_TIMES) { // 提前结案
                noUse = records.stream().skip(times).collect(Collectors.toList());
                records = records.stream().limit(times).collect(Collectors.toList());
            }
        } else if (FOUR.equals(record.getStatus())) { // CONTINUE
            records.add(new Record("", FIVE, null, null, null));
        }

        return noUse;
    }

    public void arrangeTheLastRecord(final Record record) {
        records.set(records.size() - 1, record);
    }

    public boolean isFinishedAdvanced() {
        return !ZERO.equals(consultStatus) && times < REGULAR_TIMES;
    }
}
