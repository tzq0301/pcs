package cn.tzq0301.duty.entity.work;

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
public class WorkResponse {
    private List<InnerWorkItem> works;

    public WorkResponse(List<WorkItem> works) {
        this.works = works.stream()
                .map(workItem -> new InnerWorkItem(DateUtils.localDateToString(workItem.getDay()),
                        workItem.getFrom(), workItem.getAddress()))
                .collect(Collectors.toList());
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class InnerWorkItem {
        private String day;

        private Integer from;

        private String address;
    }
}
