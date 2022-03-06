package cn.tzq0301.duty.entity.duty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author tzq0301
 * @version 1.0
 */
@Data
@NoArgsConstructor
public class Pattern implements Serializable {
    private static final long serialVersionUID = -2792815661222461967L;

    private Integer weekday;

    private Integer from;

    private String address;

    Pattern(Integer weekday, Integer from, String address) {
        this.weekday = weekday;
        this.from = from;
        this.address = address;
    }
}
