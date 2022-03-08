package cn.tzq0301.duty.handler;

import cn.tzq0301.duty.entity.duty.*;
import cn.tzq0301.duty.entity.duty.vo.DutyResponse;
import cn.tzq0301.duty.entity.work.WorkItems;
import cn.tzq0301.duty.entity.work.WorkResponse;
import cn.tzq0301.duty.entity.work.Works;
import cn.tzq0301.duty.service.DutyService;
import cn.tzq0301.result.Result;
import cn.tzq0301.util.DateUtils;
import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

import static cn.tzq0301.util.Num.ZERO;
import static java.util.Objects.requireNonNull;

/**
 * @author tzq0301
 * @version 1.0
 */
@Component
@AllArgsConstructor
@Log4j2
public class DutyHandler {
    private final DutyService dutyService;

    public Mono<ServerResponse> findAddressByUserIdAndDayAndFrom(ServerRequest request) {
        String userId = request.pathVariable("user_id");
        LocalDate day = DateUtils.stringToLocalDate(request.pathVariable("day"));
        int from = Integer.parseInt(request.pathVariable("from"));

        return dutyService.findDutyByUserId(userId)
                .switchIfEmpty(Mono.just(Duties.newDuty(userId)))
                .doOnNext(dutyService::saveDuty)
                .map(duty -> {
                    Optional<String> address = duty.getSpecials().stream()
                            .filter(specialItem -> ZERO.equals(specialItem.getType()))
                            .filter(specialItem -> Objects.equals(day, specialItem.getDay()))
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

    public Mono<ServerResponse> findDutyByUserId(ServerRequest request) {
        return dutyService.findDutyByUserId(request.pathVariable("user_id"))
                .map(duty -> new DutyResponse(duty.getPatterns(), duty.getSpecials()))
                .map(Result::success)
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> findWorkByUserId(ServerRequest request) {
        return dutyService.findWorkByUserId(request.pathVariable("user_id"))
                .map(work -> new WorkResponse(work.getWorks()))
                .map(Result::success)
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> findAllDuties(ServerRequest request) {
        long offset = Long.parseLong(requireNonNull(getAttributeFromServerRequest(request, "offset")));
        long limit = Long.parseLong(requireNonNull(getAttributeFromServerRequest(request, "limit")));
        String subname = getAttributeFromServerRequest(request, "subname");

        return dutyService.findAllDuties()
                .doOnNext(allDutyDetails -> {
                    if (Strings.isNullOrEmpty(subname)) {
                        allDutyDetails.page(offset, limit);
                    } else {
                        allDutyDetails.page(offset, limit, s -> s.contains(subname));
                    }
                })
                .map(Result::success)
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> addWorkOvertimeRecord(ServerRequest request) {
        String userId = request.pathVariable("user_id");
        LocalDate day = DateUtils.stringToLocalDate(request.pathVariable("day"));
        int from = Integer.parseInt(request.pathVariable("from"));
        String address = request.pathVariable("address");
        int type = Integer.parseInt(request.pathVariable("type"));

        return dutyService.findDutyByUserId(userId)
                .flatMap(duty -> {
                    boolean isSuccess = duty.addSpecial(SpecialItems.newSpecialItem(day, from, address, type));
                    if (!isSuccess) {
                        return Mono.empty();
                    }
                    return dutyService.saveDuty(duty);
                })
                .doOnNext(duty -> log.info("Save Duty -> {}", duty))
                .flatMap(duty -> dutyService.findWorkByUserId(userId)
                        .switchIfEmpty(Mono.just(Works.newWork(userId)))
                        .flatMap(work -> {
                            boolean isSuccess = work.addWork(WorkItems.newWorkItem(day, from, address));
                            if (!isSuccess) {
                                return Mono.empty();
                            }
                            return dutyService.saveWork(work);
                        }))
                .doOnNext(work -> log.info("Save Work -> {}", work))
                .map(it -> Result.success())
                .switchIfEmpty(Mono.just(Result.error()))
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> removeWorkOvertimeRecord(ServerRequest request) {
        String userId = request.pathVariable("user_id");
        LocalDate day = DateUtils.stringToLocalDate(request.pathVariable("day"));
        int from = Integer.parseInt(request.pathVariable("from"));
        int type = Integer.parseInt(request.pathVariable("type"));

        return dutyService.findDutyByUserId(userId)
                .flatMap(duty -> {
                    boolean isSuccess = duty.removeSpecial(SpecialItems.newSpecialItem(day, from, "", type));
                    if (!isSuccess) {
                        return Mono.empty();
                    }
                    return  dutyService.saveDuty(duty);
                })
                .doOnNext(duty -> log.info("Save Duty: {}", duty))
                .flatMap(duty -> dutyService.findWorkByUserId(userId)
                        .flatMap(work -> {
                            boolean isSuccess = work.removeWork(day, from);
                            if (!isSuccess) {
                                return Mono.empty();
                            }
                            return dutyService.saveWork(work);
                        }))
                .doOnNext(work -> log.info("Save Work -> {}", work))
                .map(it -> Result.success())
                .switchIfEmpty(Mono.just(Result.error()))
                .flatMap(ServerResponse.ok()::bodyValue);

    }

    private static String getAttributeFromServerRequest(ServerRequest request, String attribute) {
        return request.exchange().getRequest().getQueryParams().getFirst(attribute);
    }
}
