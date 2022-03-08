package cn.tzq0301.consult.entity.consultor;

import cn.tzq0301.consult.entity.Pattern;
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
public class ConsultRecordOfConsultor {
    private String globalId; // Global ID

    private String studentId; // 学生 ID

    private String studentName; // 学生姓名

    private String studentPhone; // 学生电话

    private Integer times; // 已进行过的咨询次数

    private Integer consultStatus; // 咨询状态（正在进行为 0、已完成但未填总结为 1、已完成且已填总结为 2）

    private Pattern pattern;
}
