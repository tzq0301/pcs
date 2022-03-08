package cn.tzq0301.consult.entity;

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
@AllArgsConstructor
public class Pattern implements Serializable {
    private static final long serialVersionUID = 5114211011516210263L;

    private Integer weekday;

    private Integer from;

    private String address;
}
