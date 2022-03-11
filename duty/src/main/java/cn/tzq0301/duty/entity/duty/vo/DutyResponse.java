package cn.tzq0301.duty.entity.duty.vo;

import cn.tzq0301.duty.entity.duty.Pattern;
import cn.tzq0301.duty.entity.duty.SpecialItem;
import cn.tzq0301.util.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author tzq0301
 * @version 1.0
 */
@Data
@NoArgsConstructor
public class DutyResponse {
    private List<Pattern> patterns;

    private List<DutySpecialItem> specials;

    public DutyResponse(List<Pattern> patterns, List<SpecialItem> specials) {
        this.patterns = patterns;
        this.specials = specials.stream()
                .map(item -> new DutySpecialItem(DateUtils.localDateToString(item.getDay()), item.getFrom(),
                        item.getAddress(), item.getType()))
                .collect(Collectors.toList());
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class DutySpecialItem {
        private String day;

        private Integer from;

        private String address;

        private Integer type; // 特殊情况的类型（加班为 0、请假为 1）
    }
}
