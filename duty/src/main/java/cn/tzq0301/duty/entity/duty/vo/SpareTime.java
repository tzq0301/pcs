package cn.tzq0301.duty.entity.duty.vo;

import cn.tzq0301.duty.entity.duty.Pattern;
import cn.tzq0301.duty.entity.duty.SpecialItem;
import cn.tzq0301.duty.entity.work.Work;
import cn.tzq0301.duty.entity.work.WorkItem;
import cn.tzq0301.util.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author tzq0301
 * @version 1.0
 */
@Data
@NoArgsConstructor
public class SpareTime {
    private List<Pattern> patterns;

    private List<SpareSpecial> specials;

    private List<SpareWork> works;

    public SpareTime(List<Pattern> patterns, List<SpecialItem> specials, List<WorkItem> works) {
        this.patterns = patterns;
        this.specials = specials.stream()
                .map(specialItem -> new SpareSpecial(DateUtils.localDateToString(specialItem.getDay()),
                        specialItem.getFrom(), specialItem.getAddress(), specialItem.getType()))
                .collect(Collectors.toList());
        this.works = works.stream()
                .map(work -> new SpareWork(DateUtils.localDateToString(work.getDay()), work.getFrom(), work.getAddress()))
                .collect(Collectors.toList());
    }

    @Data
    static class SpareSpecial {
        String day;

        Integer from;

        String address;

        Integer type;

        public SpareSpecial(String day, Integer from, String address, Integer type) {
            this.day = day;
            this.from = from;
            this.address = address;
            this.type = type;
        }
    }

    @Data
    static class SpareWork {
        String day;

        Integer from;

        String address;

        public SpareWork(String day, Integer from, String address) {
            this.day = day;
            this.from = from;
            this.address = address;
        }
    }
}
