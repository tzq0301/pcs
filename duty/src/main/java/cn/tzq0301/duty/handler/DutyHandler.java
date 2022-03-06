package cn.tzq0301.duty.handler;

import cn.tzq0301.duty.entity.duty.Duties;
import cn.tzq0301.duty.entity.duty.Pattern;
import cn.tzq0301.duty.entity.duty.SpecialItem;
import cn.tzq0301.duty.service.DutyService;
import cn.tzq0301.util.DateUtils;
import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static cn.tzq0301.util.Num.ZERO;

/**
 * @author tzq0301
 * @version 1.0
 */
@Component
@AllArgsConstructor
public class DutyHandler {
    private final DutyService dutyService;

    public Mono<ServerResponse> getAddressByUserIdAndDayAndFrom(ServerRequest request) {
        String userId = request.pathVariable("user_id");
        LocalDate day = DateUtils.stringToLocalDate(request.pathVariable("day"));
        int from = Integer.parseInt(request.pathVariable("from"));

        return dutyService.getDutyByUserId(userId)
                .switchIfEmpty(Mono.just(Duties.newDuty(userId)))
                .doOnNext(dutyService::saveDuty)
                .map(duty -> {
                    Optional<String> address = duty.getSpecials().stream()
                            .filter(specialItem -> ZERO.equals(specialItem.getType()))
                            .filter(specialItem -> Objects.equals(day, specialItem.getDate()))
                            .filter(specialItem -> Objects.equals(from, specialItem.getFrom()))
                            .findAny()
                            .map(SpecialItem::getAddress);

                    if (address.isPresent()) {
                        return address.get();
                    }

                    int weekday = day.getDayOfWeek().getValue();

                    return duty.getPatterns().stream()
                            .filter(pattern -> Objects.equals(weekday, pattern.getWeekday()))
                            .filter(pattern -> Objects.equals(from, pattern.getFrom()))
                            .findAny()
                            .map(Pattern::getAddress)
                            .orElse("");
                })
                .flatMap(ServerResponse.ok()::bodyValue);
    }
}
