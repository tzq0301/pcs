package cn.tzq0301.duty.handler;

import cn.tzq0301.duty.entity.duty.*;
import cn.tzq0301.duty.entity.duty.vo.DutyResponse;
import cn.tzq0301.duty.entity.work.WorkItems;
import cn.tzq0301.duty.entity.work.WorkResponse;
import cn.tzq0301.duty.service.DutyService;
import cn.tzq0301.result.Result;
import cn.tzq0301.util.DateUtils;
import cn.tzq0301.util.Num;
import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cglib.core.Local;
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

    public Mono<ServerResponse> addWorkItemAndReturn(ServerRequest request) {
        int weekday = Integer.parseInt(request.pathVariable("weekday"));
        int from = Integer.parseInt(request.pathVariable("from"));
        String address = request.pathVariable("address");


        return dutyService.addWorkByUserIdAndReturn(request.pathVariable("user_id"), weekday, from, address)
                .flatMap(it -> ServerResponse.ok().bodyValue(it.getDay()));
    }

    public Mono<ServerResponse> deleteWorkItem(ServerRequest request) {
        return dutyService.deleteWorkByUserId(request.pathVariable("user_id"), WorkItems.newWorkItem(
                        request.pathVariable("day"), Integer.parseInt(request.pathVariable("from")),
                        request.pathVariable("address")))
                .flatMap(it -> ServerResponse.ok().bodyValue("OK"));
    }

    /**
     * ??????????????????
     *
     * @param request ??????
     * @return ??????
     */
    public Mono<ServerResponse> addRegularDuty(ServerRequest request) {
        String userId = request.pathVariable("user_id");
        int weekday = Integer.parseInt(request.pathVariable("weekday"));
        int from = Integer.parseInt(request.pathVariable("from"));
        String address = request.pathVariable("address");

        log.info("????????????????????????{} {}:00~{}:00 {}", Num.getChinese(weekday), from, from + 1, address);

        return dutyService.addRegularDutyByUserId(userId, Patterns.newPattern(weekday, from, address))
                .map(Result::success)
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    /**
     * ??????????????????
     *
     * @param request ??????
     * @return ??????
     */
    public Mono<ServerResponse> removeRegularDuty(ServerRequest request) {
        String userId = request.pathVariable("user_id");
        int weekday = Integer.parseInt(request.pathVariable("weekday"));
        int from = Integer.parseInt(request.pathVariable("from"));

        return dutyService.removeRegularDuty(userId, Patterns.newPattern(weekday, from, ""))
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
        long offset = getOffset(request);
        long limit = getLimit(request);
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
                    log.info("??????{}?????????{} {}:00~{}:00 {}",
                            type == 1 ? "??????" : "??????", day.toString(), from, from + 1, address);
                    return dutyService.saveDuty(duty);
                })
                .doOnNext(duty -> log.info("Save Duty -> {}", duty))
                .map(it -> Result.success())
                .switchIfEmpty(Mono.just(Result.error()))
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> removeWorkOvertimeRecord(ServerRequest request) {
        String userId = request.pathVariable("user_id");
        LocalDate day = DateUtils.stringToLocalDate(request.pathVariable("day"));
        int from = Integer.parseInt(request.pathVariable("from"));

        return dutyService.findDutyByUserId(userId)
                .doOnNext(duty -> log.info("Got Duty: {}", duty))
                .flatMap(duty -> {
                    boolean isSuccess = duty.removeSpecial(day, from);
                    if (!isSuccess) {
                        return Mono.empty();
                    }
                    return dutyService.saveDuty(duty);
                })
                .doOnNext(duty -> log.info("Save Duty: {}", duty))
//                .flatMap(duty -> dutyService.findWorkByUserId(userId)
//                        .flatMap(work -> {
//                            boolean isSuccess = work.removeWork(day, from);
//                            if (!isSuccess) {
//                                return Mono.empty();
//                            }
//                            return dutyService.saveWork(work);
//                        }))
//                .doOnNext(work -> log.info("Save Work -> {}", work))
                .map(it -> Result.success())
                .switchIfEmpty(Mono.just(Result.error()))
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> addWorkItemOfTimesForUser(ServerRequest request) {
        String userId = request.pathVariable("user_id");
        int weekday = Integer.parseInt(request.pathVariable("weekday"));
        int from = Integer.parseInt(request.pathVariable("from"));
        String address = request.pathVariable("address");
        int times = Integer.parseInt(request.pathVariable("times"));

        return dutyService.addWorkItemOfTimesForUser(userId, weekday, from, address, times)
                .collectList()
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> listSpareVisitorsByDay(ServerRequest request) {
        String dayAttribute = getAttributeFromServerRequest(request, "day");
        String fromAttribute = getAttributeFromServerRequest(request, "from");

        if ((Strings.isNullOrEmpty(dayAttribute) && !Strings.isNullOrEmpty(fromAttribute))
                || (!Strings.isNullOrEmpty(dayAttribute) && Strings.isNullOrEmpty(fromAttribute))) {
            return ServerResponse.ok().bodyValue(Result.error());
        }

        if (Strings.isNullOrEmpty(dayAttribute) && Strings.isNullOrEmpty(fromAttribute)) {
            return dutyService.listSpareVisitors()
                    .map(Result::success)
                    .flatMap(ServerResponse.ok()::bodyValue);
        }

        return dutyService.listSpareVisitorsByDay(DateUtils.stringToLocalDate(dayAttribute), Integer.parseInt(fromAttribute))
                .map(Result::success)
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    private static String getAttributeFromServerRequest(ServerRequest request, String attribute) {
        return request.exchange().getRequest().getQueryParams().getFirst(attribute);
    }

    public Mono<ServerResponse> listSpareTimesById(ServerRequest request) {
        return dutyService.listSpareTimesById(request.pathVariable("user_id"))
                .map(Result::success)
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> listNonSpareAddressesByWeekday(ServerRequest request) {
        return dutyService.listNonSpareAddressesByWeekday(
                        Integer.parseInt(request.pathVariable("weekday")),
                        Integer.parseInt(request.pathVariable("from")))
                .doOnNext(nonSpareAddresses -> log.info("Non Spare Addresses -> {}", nonSpareAddresses))
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> listNonSpareAddressesByDay(ServerRequest request) {
        return dutyService.listNonSpareAddressesByDay(
                        DateUtils.stringToLocalDate(request.pathVariable("day")),
                        Integer.parseInt(request.pathVariable("from")))
                .doOnNext(nonSpareAddresses -> log.info("Non Spare Addresses -> {}", nonSpareAddresses))
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    /**
     * ???????????????????????? offset
     *
     * @return offset??????????????? 0???
     */
    private int getOffset(ServerRequest request) {
        String offset = request.exchange().getRequest().getQueryParams().getFirst("offset");

        if (Strings.isNullOrEmpty(offset)) {
            return 0;
        }

        return Integer.parseInt(offset);
    }

    /**
     * ???????????????????????? limit
     *
     * @return limit??????????????? {@code Integer.MAX_VALUE}???
     */
    private int getLimit(ServerRequest request) {
        String limit = request.exchange().getRequest().getQueryParams().getFirst("limit");

        if (Strings.isNullOrEmpty(limit)) {
            return Integer.MAX_VALUE;
        }

        return Integer.parseInt(limit);
    }

    /**
     * ???????????????????????? str
     *
     * @return str
     */
    private String getStr(ServerRequest request) {
        return request.exchange().getRequest().getQueryParams().getFirst("str");
    }
}
