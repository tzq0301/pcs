package cn.tzq0301.statics.entity;

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
public class CsvInfo implements Serializable {
    private static final long serialVersionUID = 6252928030237900862L;

    private String name;

    private String gender;

    private String phone;

    private String email;

    private Long numOfPeople;

    private Long numOfTime;
}
