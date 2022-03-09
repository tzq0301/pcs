package cn.tzq0301.duty.entity.duty;

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
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import static cn.tzq0301.util.Num.ONE;
import static cn.tzq0301.util.Num.ZERO;

/**
 * @author tzq0301
 * @version 1.0
 */
@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
@Log4j2
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
        if (this.patterns.contains(pattern)) {
            return;
        }

        this.patterns.add(pattern);
    }

    public void removePattern(final Pattern pattern) {
        if (!this.patterns.contains(pattern)) {
            return;
        }

        this.patterns.remove(pattern);
    }

    public boolean addSpecial(final SpecialItem specialItem) {
        if (this.specials.contains(specialItem)) {
            log.info("已有该记录，无需重复添加");
            return false;
        }

        // 如果是加班记录，且该时间段已有请假记录，则说明该时间段符合 patterns，直接删去请假记录即可
        if (ZERO.equals(specialItem.getType())) {
            Iterator<SpecialItem> iterator = this.specials.iterator();
            while (iterator.hasNext()) {
                SpecialItem special = iterator.next();
                if (ONE.equals(special.getType())
                        && Objects.equals(special.getDay(), specialItem.getDay())
                        && Objects.equals(special.getFrom(), specialItem.getFrom())) {
                    iterator.remove();
                    log.info("已有请假记录，撤销该请假记录");
                    return true;
                }
            }
        }

        // 如果是加班记录，则判断该加班时间是否符合 patterns，如果符合，则说明这个时间是正常上班时间，不应该被作为加班记录
        if (ZERO.equals(specialItem.getType())) {
            for (Pattern pattern : this.patterns) {
                if (Objects.equals(pattern.getWeekday(), specialItem.getDay().getDayOfWeek().getValue())
                        && Objects.equals(pattern.getFrom(), specialItem.getFrom())) {
                    log.info("该时间为正常上班时间，不能加班");
                    return false;
                }
            }
        }

        // 如果是请假记录，则判断该请假时间是否符合 patterns，如果不符合，则说明这个时间不是正常上班时间，不用请假
        if (ONE.equals(specialItem.getType())) {
            boolean matchPattern = false;
            for (Pattern pattern : this.patterns) {
                if (Objects.equals(pattern.getWeekday(), specialItem.getDay().getDayOfWeek().getValue())
                        && Objects.equals(pattern.getFrom(), specialItem.getFrom())) {
                    matchPattern = true;
                    break;
                }
            }
            if (!matchPattern) {
                log.info("该时间不是正常上班时间，不需要请假");
                return false;
            }
        }

        this.specials.add(specialItem);

        log.info("成功添加记录 {}", specialItem);

        return true;
    }

    public boolean removeSpecial(final SpecialItem specialItem) {
        Iterator<SpecialItem> iterator = this.specials.iterator();
        while (iterator.hasNext()) {
            SpecialItem item = iterator.next();
            if (Objects.equals(item.getDay(), specialItem.getDay())
                    && Objects.equals(item.getFrom(), specialItem.getFrom())) {
                iterator.remove();
                return true;
            }
        }

        return false;
    }
}
