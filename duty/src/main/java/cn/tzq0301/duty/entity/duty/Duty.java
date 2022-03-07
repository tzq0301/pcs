package cn.tzq0301.duty.entity.duty;

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
public class Duty implements Serializable {
    private static final long serialVersionUID = -4639775267538393419L;

    @Id
    @Field("_id")
    @MongoId(FieldType.OBJECT_ID)
    private ObjectId id;

    private String userId;

    private List<Pattern> patterns;

    private List<SpecialItem> specials;

    Duty(String userId) {
        this.userId = userId;
        this.patterns = Lists.newArrayList();
        this.specials = Lists.newArrayList();
    }

    Duty(String userId, List<Pattern> patterns, List<SpecialItem> specials) {
        this.userId = userId;
        this.patterns = patterns;
        this.specials = specials;
    }

    public void addPattern(final Pattern pattern) {
        this.patterns.add(pattern);
    }

    public void addSpecial(final SpecialItem specialItem) {
        this.specials.add(specialItem);
    }
}
