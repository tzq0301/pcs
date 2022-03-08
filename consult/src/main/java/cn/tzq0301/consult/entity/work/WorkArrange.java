package cn.tzq0301.consult.entity.work;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * @author tzq0301
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkArrange {
    private LocalDate day;

    private Integer weekday;

    private Integer from;

    private String address;
}
