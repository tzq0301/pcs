package cn.tzq0301.duty.entity.duty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author tzq0301
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpecialItem implements Serializable {
    private static final long serialVersionUID = 3116398446713742957L;

    private LocalDate day;

    private Integer from;

    private String address;

    private Integer type; // 特殊情况的类型（加班为 0、请假为 1）
}
