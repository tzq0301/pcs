package cn.tzq0301.duty.entity.work;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.io.Serializable;
import java.util.List;

/**
 * @author tzq0301
 * @version 1.0
 */
@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
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

    public void deleteWork(final WorkItem workItem) {
        this.works.remove(workItem);
    }
}
