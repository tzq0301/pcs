package cn.tzq0301.statics.entity;

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
public class ConsultRecord {
    private static final long serialVersionUID = -5131665306735863192L;

    private String date;

    private String location;

    private String content;
}
