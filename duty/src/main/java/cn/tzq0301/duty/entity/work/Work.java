package cn.tzq0301.duty.entity.work;

import cn.tzq0301.duty.entity.duty.SpecialItem;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * @author tzq0301
 * @version 1.0
 */
@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
@Log4j2
public class Work implements Serializable {
    private static final long serialVersionUID = 1829097272373289004L;

    @Id
    @Field("_id")
    @MongoId(FieldType.OBJECT_ID)
    private ObjectId id;

    private String userId;

    private List<WorkItem> works;

    Work(String userId) {
        this.userId = userId;
        this.works = Lists.newArrayList();
    }

    public boolean addWork(final WorkItem workItem) {
        if (this.works.contains(workItem)) {
            return false;
        }

        this.works.add(workItem);

        return true;
    }

    public boolean removeWork(final WorkItem workItem) {

        boolean isRemoveSuccess = this.works.removeIf(item -> Objects.equals(item.getDay(), workItem.getDay())
                && Objects.equals(item.getFrom(), workItem.getFrom())
                && Objects.equals(item.getAddress(), workItem.getAddress()));

        if (!isRemoveSuccess) {
            log.info("Cannot remove WorkItem -> {}", workItem);
            return false;
        }

        return true;
    }

    public boolean removeWork(final LocalDate day, final Integer from) {
        boolean isRemoveSuccess = this.works.removeIf(
                item -> Objects.equals(item.getDay(), day) && Objects.equals(item.getFrom(), from));

        if (!isRemoveSuccess) {
            return false;
        }

        return true;
    }

    public Work arrangeWorks(final int weekday, final int from, final String address, int times) {
        LocalDate day = LocalDate.now();
        int left = times;

        while (day.getDayOfWeek().getValue() != weekday) {
            day = day.plusDays(1);
        }

        while (left > 0) {
            if (!hasPreviousWorkArrange(day, from)) {
                addWork(WorkItems.newWorkItem(day, from, address));
                left--;
            }
            day = day.plusWeeks(1);
        }

        log.info("Add works: {}", this.works.subList(this.works.size() - times, this.works.size()));

        return this;
    }

    private boolean hasPreviousWorkArrange(final LocalDate day, int from) {
        return this.works.stream()
                .anyMatch(workItem -> Objects.equals(workItem.getDay(), day)
                        && Objects.equals(workItem.getFrom(), from));
    }
}
