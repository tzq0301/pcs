package cn.tzq0301.visit.apply.handler;

import cn.tzq0301.entity.Records;
import cn.tzq0301.entity.RecordsWithTotal;
import cn.tzq0301.result.Result;
import cn.tzq0301.util.DateUtils;
import cn.tzq0301.util.JWTUtils;
import cn.tzq0301.util.PageUtils;
import cn.tzq0301.visit.apply.entity.Applies;
import cn.tzq0301.visit.apply.entity.ProblemEnum;
import cn.tzq0301.visit.apply.entity.applyrequest.ApplyRequest;
import cn.tzq0301.visit.apply.entity.applyrequest.ApplyRequestException;
import cn.tzq0301.visit.apply.entity.applyrequest.ApplyRequestResult;
import cn.tzq0301.visit.apply.entity.getapply.GetApply;
import cn.tzq0301.visit.apply.entity.getapply.GetApplyResult;
import cn.tzq0301.visit.apply.entity.passapply.PassApplyRequest;
import cn.tzq0301.visit.apply.service.ApplyService;
import cn.tzq0301.visit.record.service.VisitRecordService;
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

import static cn.tzq0301.result.DefaultResultEnum.SUCCESS;
import static cn.tzq0301.util.Num.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * @author tzq0301
 * @version 1.0
 */
@Component
@AllArgsConstructor
@Log4j2
public class ApplyHandler {
    private final ApplyService applyService;

    private final VisitRecordService visitRecordService;

    public Mono<ServerResponse> requestApply(ServerRequest request) {
        String jwt = getJWT(request);
//        String userId = getUserId(jwt);

        return applyService.requestApply(jwt, request.bodyToMono(ApplyRequest.class))
//                .flatMap(apply -> ServerResponse.created(
//                        URI.create("/visit/id/" + userId + "/apply_id/" + apply.getId())).build())
                .flatMap(it -> ServerResponse.ok().bodyValue(Result.success(2, "????????????")))
                .onErrorResume(ApplyRequestException.class, error ->
                        ServerResponse.ok().bodyValue(Result.error(ApplyRequestResult.NO_NEED_TO_APPLY_TWICE)));
    }

    public Mono<ServerResponse> getApply(ServerRequest request) {
        String userIdFromJWT = getUserId(request);
        String userId = request.pathVariable("id");

        if (!Objects.equals(userId, userIdFromJWT)) {
            log.info("\nIn Path: id = {}\nIn JWT:  id = {}", userId, userIdFromJWT);
            return ServerResponse.ok().bodyValue(Result.error(2, "?????? ID ?????????"));
        }

        String applyId = request.pathVariable("apply_id");

        return applyService.getApplyByApplyId(applyId)
                .map(apply -> new GetApply(apply.getPhone(), apply.getEmail(),
                        ProblemEnum.getName(apply.getProblemId()), apply.getProblemDetail(),
                        DateUtils.localDateToString(apply.getDay()),
                        apply.getFrom(), apply.getAddress()))
                .map(apply -> Result.success(apply, GetApplyResult.SUCCESS))
                .flatMap(ServerResponse.ok()::bodyValue)
                .switchIfEmpty(ServerResponse.ok().bodyValue(Result.error(GetApplyResult.APPLY_NOT_FOUNT)));
    }

    /**
     * ?????????????????????????????????
     *
     * <ol>
     *     <li>?????? JWT ?????? userId ?????????????????? id ???????????????????????????????????????</li>
     *     <li>?????? userId ??????????????????</li>
     *     <li>?????????????????????</li>
     * </ol>
     *
     * @param request ??????
     * @return ??????
     */
    public Mono<ServerResponse> getApplies(ServerRequest request) {
        String userIdFromJWT = getUserId(request);
        String userId = request.pathVariable("id");

        if (!Objects.equals(userId, userIdFromJWT)) {
            log.info("\nIn Path: id = {}\nIn JWT:  id = {}", userId, userIdFromJWT);
            return ServerResponse.ok().bodyValue(Result.error(2, "?????? ID ?????????"));
        }

        int offset = getOffset(request);
        int limit = getLimit(request);

        return applyService.getAppliesByUserId(userId)
                .map(Applies::toGetApplies)
                .collectList()
                .map(list -> new RecordsWithTotal<>(list, offset, limit))
                .flatMap(applies -> ServerResponse.ok().bodyValue(Result.success(applies, SUCCESS)));

//        return PageUtils.pagingFlux(applyService.getAppliesByUserId(userId), offset, limit, Applies::toGetApplies)
//                .map(Records::new)
//                .flatMap(applies -> ServerResponse.ok().bodyValue(Result.success(applies, SUCCESS)));
    }

    public Mono<ServerResponse> getAllUnfinishedApplies(ServerRequest request) {
        int offset = getOffset(request);
        int limit = getLimit(request);
        String str = getStr(request);

        return applyService.getAllUnfinishedApplies()
                .collectList()
                .map(unfinishedApplies -> Strings.isNullOrEmpty(str)
                        ? new RecordsWithTotal<>(unfinishedApplies, offset, limit)
                        : new RecordsWithTotal<>(unfinishedApplies,
                        apply -> apply.getStudentName().contains(str) || apply.getVisitorName().contains(str),
                        offset, limit))
                .flatMap(it -> ServerResponse.ok().bodyValue(Result.success(it, SUCCESS)));
    }

    public Mono<ServerResponse> getAllApplies(ServerRequest request) {
        int offset = getOffset(request);
        int limit = getLimit(request);
        String str = getStr(request);

        return applyService.getAllApplies()
                .collectList()
                .map(firstRecords -> Strings.isNullOrEmpty(str)
                        ? new RecordsWithTotal<>(firstRecords, offset, limit)
                        : new RecordsWithTotal<>(firstRecords,
                        apply -> apply.getStudentName().contains(str) || apply.getVisitorName().contains(str),
                        offset, limit))
                .flatMap(it -> ServerResponse.ok().bodyValue(Result.success(it, SUCCESS)));
    }

    /**
     * ??????????????????????????????
     * <p>
     * ?????????????????????????????????
     * <ul>
     *     <li>????????????????????????</li>
     *     <li>??????????????????????????????????????????????????????</li>
     * </ul>
     *
     * @param request ??????
     * @return ??????
     */
    public Mono<ServerResponse> deleteApplyById(ServerRequest request) {
        String userIdFromJWT = getUserId(request);
        String userId = request.pathVariable("user_id");

        if (!Objects.equals(userId, userIdFromJWT)) {
            log.info("In Path: id = {}", userId);
            log.info("In JWT:  id = {}", userIdFromJWT);
            return Mono.just(Result.error(2, "?????? ID ?????????"))
                    .doOnNext(result -> log.info("{}", result))
                    .flatMap(ServerResponse.ok()::bodyValue);
        }

        String applyId = request.pathVariable("global_id");

        return applyService.getApplyByApplyId(applyId)
                .doOnNext(apply -> log.info("Got Apply: {}", apply.toString()))
                .flatMap(apply -> {
                    if (!Objects.equals(userId, apply.getUserId())) {
                        return Mono.just(Result.error(4, "???????????????????????????????????? ID ??? Global ID ????????????"));
                    }

                    if (TWO.equals(apply.getStatus())) {
                        return Mono.just(Result.error(5, "????????????????????????????????????????????????"));
                    }

                    if (THREE.equals(apply.getStatus())) {
                        return Mono.just(Result.error(6, "??????????????????????????????????????????????????????"));
                    }

                    log.info("Global ID: {}", apply.getId().toHexString());
                    return visitRecordService.findVisitRecordById(apply.getId())
                            .doOnNext(visitRecord -> log.info("Got VisitRecord: {}", visitRecord.toString()))
                            .flatMap(record -> {
                                if (ONE.equals(apply.getStatus())
                                        && LocalDate.now().plusDays(1).isAfter(record.getDay())) {
                                    return Mono.just(Result.error(1, "???????????????????????????????????????????????????????????????"));
                                }

                                return applyService.revokeApply(apply)
                                        .doOnNext(tuple -> log.info("?????? {} ???????????? Apply: {}", tuple.getT2(), tuple.getT1()))
                                        .map(it -> Result.success(0, "????????????"));
                            })
                            .switchIfEmpty(applyService.revokeUnPassedApply(apply)
                                    .doOnNext(it -> log.info("?????? Apply: {}", apply))
                                    .map(it -> Result.success(0, "????????????")));
                })
                .switchIfEmpty(Mono.just(Result.error(3, "???????????????????????????")))
                .doOnNext(result -> log.info("{}", result))
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> passApply(ServerRequest request) {
        return request.bodyToMono(PassApplyRequest.class)
                .flatMap(applyService::passApply)
                .flatMap(it -> ServerResponse.ok().build());
    }

    public Mono<ServerResponse> rejectApply(ServerRequest request) {
        return applyService.rejectApply(request.pathVariable("apply_id"))
                .map(it -> Result.success())
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> deleteApplyByGlobalId(ServerRequest request) {
        return applyService.deleteApplyById(request.pathVariable("global_id"))
                .flatMap(it -> ServerResponse.ok().build())
                .switchIfEmpty(ServerResponse.ok().build());
    }

    private String getJWT(ServerRequest request) {
        return Objects.requireNonNull(request.headers().firstHeader(AUTHORIZATION))
                .substring(JWTUtils.AUTHORIZATION_HEADER_PREFIX.length());
    }

    private String getUserId(String jwt) {
        return JWTUtils.extractUserId(jwt);
    }

    private String getUserId(ServerRequest request) {
        return JWTUtils.extractUserId(Objects.requireNonNull(
                        request.headers().firstHeader(AUTHORIZATION))
                .substring(JWTUtils.AUTHORIZATION_HEADER_PREFIX.length()));
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
