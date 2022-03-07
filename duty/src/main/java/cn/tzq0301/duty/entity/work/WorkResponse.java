package cn.tzq0301.duty.entity.work;

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
public class WorkResponse {
    private List<WorkItem> works;
}
