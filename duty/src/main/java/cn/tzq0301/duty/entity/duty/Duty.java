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
import java.time.LocalDate;
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
        Iterator<Pattern> iterator = this.patterns.iterator();
        while (iterator.hasNext()) {
            Pattern next = iterator.next();
            if (Objects.equals(next.getWeekday(), pattern.getWeekday())
            && Objects.equals(next.getFrom(), pattern.getFrom())) {
                iterator.remove();
                return;
            }
        }

        this.patterns.remove(pattern);
    }

    public boolean addSpecial(final SpecialItem specialItem) {
        if (this.specials.contains(specialItem)) {
            log.info("????????????????????????????????????");
            return false;
        }

        // ??????????????????????????????????????????????????????????????????????????????????????? patterns?????????????????????????????????
        if (ZERO.equals(specialItem.getType())) {
            Iterator<SpecialItem> iterator = this.specials.iterator();
            while (iterator.hasNext()) {
                SpecialItem special = iterator.next();
                if (ONE.equals(special.getType())
                        && Objects.equals(special.getDay(), specialItem.getDay())
                        && Objects.equals(special.getFrom(), specialItem.getFrom())) {
                    iterator.remove();
                    log.info("??????????????????????????????????????????");
                    return true;
                }
            }
        }

        // ???????????????????????????????????????????????????????????? patterns?????????????????????????????????????????????????????????????????????????????????????????????
        if (ZERO.equals(specialItem.getType())) {
            for (Pattern pattern : this.patterns) {
                if (Objects.equals(pattern.getWeekday(), specialItem.getDay().getDayOfWeek().getValue())
                        && Objects.equals(pattern.getFrom(), specialItem.getFrom())) {
                    log.info("?????????????????????????????????????????????");
                    return false;
                }
            }
        }

        // ???????????????????????????????????????????????????????????? patterns?????????????????????????????????????????????????????????????????????????????????
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
                log.info("???????????????????????????????????????????????????");
                return false;
            }
        }

        this.specials.add(specialItem);

        log.info("?????????????????? {}", specialItem);

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

    public boolean removeSpecial(final LocalDate day, final Integer from) {
        Iterator<SpecialItem> iterator = this.specials.iterator();
        while (iterator.hasNext()) {
            SpecialItem item = iterator.next();
            if (Objects.equals(item.getDay(), day)
                    && Objects.equals(item.getFrom(), from)) {
                iterator.remove();
                return true;
            }
        }

        return false;
    }

    public boolean isOnDuty(final LocalDate day, final Integer from) {
        return isWorkOverTime(day, from) || (!isOnLeave(day, from) && matchPattern(day, from));
    }

    public boolean isWorkOverTime(final LocalDate day, final Integer from) {
        return specials.stream()
                .anyMatch(specialItem -> ZERO.equals(specialItem.getType())
                        && Objects.equals(day, specialItem.getDay())
                        && Objects.equals(from, specialItem.getFrom()));
    }

    public boolean isOnLeave(final LocalDate day, final Integer from) {
        return specials.stream()
                .anyMatch(specialItem -> ONE.equals(specialItem.getType())
                        && Objects.equals(day, specialItem.getDay())
                        && Objects.equals(from, specialItem.getFrom()));
    }

    public boolean matchPattern(final LocalDate day, final Integer from) {
        int weekday = day.getDayOfWeek().getValue();

        return patterns.stream()
                .anyMatch(pattern -> Objects.equals(weekday, pattern.getWeekday())
                        && Objects.equals(from, pattern.getFrom()));
    }
}
