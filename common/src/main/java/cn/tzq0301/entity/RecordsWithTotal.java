package cn.tzq0301.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Flux;

import java.util.Comparator;
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
public class RecordsWithTotal<T> {
    private List<T> records;

    private Integer total;

    public RecordsWithTotal(List<T> records) {
        this.records = records;
        this.total = records.size();
    }

    public RecordsWithTotal(List<T> records, long offset, long limit) {
        this.total = records.size();

        this.records = records.stream()
                .skip(offset * limit)
                .limit(limit)
                .collect(Collectors.toList());
    }

    public RecordsWithTotal(List<T> records, Predicate<T> predicate, long offset, long limit) {
        this.records = records.stream()
                .filter(predicate)
                .collect(Collectors.toList());
        this.total = this.records.size();
        this.records = this.records.stream()
                .skip(offset * limit)
                .limit(limit)
                .collect(Collectors.toList());
    }


}
