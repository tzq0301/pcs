package cn.tzq0301.consult.entity.student;

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
public class StudentConsult {
    private String globalId;

    private String consultorName;

    private String consultorPhone;

    private Integer times;

    private Integer consultStatus;
}
