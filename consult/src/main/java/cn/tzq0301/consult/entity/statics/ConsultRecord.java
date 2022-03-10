package cn.tzq0301.consult.entity.statics;

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
public class ConsultRecord implements Serializable {
    private static final long serialVersionUID = -5131665306735863192L;

    private String date;

    private String location;

    private String content;
}
