package cn.tzq0301.duty.entity.work;

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
public class WorkItem implements Serializable {
    private static final long serialVersionUID = 1739401814797210355L;

    private LocalDate day;

    private Integer from;

    private String address;
}
