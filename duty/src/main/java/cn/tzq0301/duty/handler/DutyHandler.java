package cn.tzq0301.duty.handler;

import cn.tzq0301.duty.entity.duty.Duties;
import cn.tzq0301.duty.entity.duty.Pattern;
import cn.tzq0301.duty.entity.duty.Patterns;
import cn.tzq0301.duty.entity.duty.SpecialItem;
import cn.tzq0301.duty.entity.work.WorkItems;
import cn.tzq0301.duty.service.DutyService;
import cn.tzq0301.util.DateUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.LocalDate;
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

    public Mono<ServerResponse> findAddressByUserIdAndDayAndFrom(ServerRequest request) {
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

    public Mono<ServerResponse> addWorkItem(ServerRequest request) {
        return dutyService.addWorkByUserId(request.pathVariable("user_id"), WorkItems.newWorkItem(
                request.pathVariable("day"), Integer.parseInt(request.pathVariable("from")),
                request.pathVariable("address")))
                .flatMap(it -> ServerResponse.ok().build());
    }

    public Mono<ServerResponse> deleteWorkItem(ServerRequest request) {
        return dutyService.deleteWorkByUserId(request.pathVariable("user_id"), WorkItems.newWorkItem(
                        request.pathVariable("day"), Integer.parseInt(request.pathVariable("from")),
                        request.pathVariable("address")))
                .flatMap(it -> ServerResponse.ok().bodyValue("OK"));
    }

    /**
     * 新增值班记录
     *
     * @param request 请求
     * @return 响应
     */
    public Mono<ServerResponse> addRegularDuty(ServerRequest request) {
        String userId = request.pathVariable("user_id");
        int weekday = Integer.parseInt(request.pathVariable("weekday"));
        int from = Integer.parseInt(request.pathVariable("from"));
        String address = request.pathVariable("address");

        return dutyService.addRegularDutyByUserId(userId, Patterns.newPattern(weekday, from, address))
                .flatMap(duty -> ServerResponse.created(URI.create("/duty/user_id/pattern/weekday/from/address")).build());
    }

    /**
     * 删除值班记录
     *
     * @param request 请求
     * @return 响应
     */
    public Mono<ServerResponse> removeRegularDuty(ServerRequest request) {
        String userId = request.pathVariable("user_id");
        int weekday = Integer.parseInt(request.pathVariable("weekday"));
        int from = Integer.parseInt(request.pathVariable("from"));
        String address = request.pathVariable("address");

        return dutyService.removeRegularDuty(userId, Patterns.newPattern(weekday, from, address))
                .flatMap(duty -> ServerResponse.noContent().build());
    }
}
