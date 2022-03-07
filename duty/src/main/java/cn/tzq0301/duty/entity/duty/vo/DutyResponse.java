package cn.tzq0301.duty.entity.duty.vo;

import cn.tzq0301.duty.entity.duty.Pattern;
import cn.tzq0301.duty.entity.duty.SpecialItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author tzq0301
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DutyResponse {
    private List<Pattern> patterns;

    private List<SpecialItem> specials;
}
