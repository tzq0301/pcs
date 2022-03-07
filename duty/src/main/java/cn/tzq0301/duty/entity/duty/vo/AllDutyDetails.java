package cn.tzq0301.duty.entity.duty.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author tzq0301
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AllDutyDetails {
    private List<DutyDetail> duties;

    private Integer total;

    public void page(final long offset, final long limit) {
        this.duties = duties.stream()
                .skip(offset * limit)
                .limit(limit)
                .collect(Collectors.toList());
    }

    public void page(final long offset, final long limit, Predicate<String> predicate) {
        this.duties = duties.stream()
                .filter(dutyDetail -> predicate.test(dutyDetail.getUserName()))
                .skip(offset * limit)
                .limit(limit)
                .collect(Collectors.toList());
    }
}
