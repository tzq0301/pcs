package cn.tzq0301.visit.apply.handler;

import cn.tzq0301.result.Result;
import cn.tzq0301.util.DateUtils;
import cn.tzq0301.util.JWTUtils;
import cn.tzq0301.util.PageUtils;
import cn.tzq0301.visit.apply.entity.Applies;
import cn.tzq0301.visit.apply.entity.Apply;
import cn.tzq0301.visit.apply.entity.applyrequest.ApplyRequest;
import cn.tzq0301.visit.apply.entity.applyrequest.ApplyRequestException;
import cn.tzq0301.visit.apply.entity.applyrequest.ApplyRequestResult;
import cn.tzq0301.visit.apply.entity.getapply.GetApply;
import cn.tzq0301.visit.apply.entity.getapply.GetApplyResult;
import cn.tzq0301.visit.apply.service.ApplyService;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

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

    public Mono<ServerResponse> requestApply(ServerRequest request) {
        String jwt = getJWT(request);
        String userId = getUserId(jwt);

        return applyService.requestApply(jwt, request.bodyToMono(ApplyRequest.class))
                .flatMap(apply -> ServerResponse.created(
                        URI.create("/visit/id/" + userId + "/apply_id/" + apply.getId())).build())
                .onErrorResume(ApplyRequestException.class, error ->
                        ServerResponse.ok().bodyValue(Result.error(ApplyRequestResult.NO_NEED_TO_APPLY_TWICE)));
    }

    public Mono<ServerResponse> getApply(ServerRequest request) {
        String userIdFromJWT = getUserId(request);
        String userId = request.pathVariable("id");

        if (!Objects.equals(userId, userIdFromJWT)) {
            log.info("\nIn Path: id = {}\nIn JWT:  id = {}", userId, userIdFromJWT);
            return ServerResponse.ok().bodyValue(Result.error(2, "用户 ID 不匹配"));
        }

        String applyId = request.pathVariable("apply_id");

        return applyService.getApplyByApplyId(applyId)
                .flatMap(apply -> ServerResponse.ok().bodyValue(Result.success(new GetApply(apply.getPhone(),
                        apply.getEmail(), apply.getProblemId(), apply.getProblemDetail(),
                        DateUtils.localDateToString(apply.getDay()),
                        apply.getFrom(), apply.getAddress()), GetApplyResult.SUCCESS)))
                .switchIfEmpty(ServerResponse.ok().bodyValue(Result.error(GetApplyResult.APPLY_NOT_FOUNT)));
    }

    /**
     * 获取所有的初访预约申请
     *
     * <ol>
     *     <li>根据 JWT 中的 userId 判断请求中的 id 是否相同，若不同则拒绝响应</li>
     *     <li>通过 userId 进行匹配查询</li>
     *     <li>对结果进行分页</li>
     * </ol>
     *
     * @param request 请求
     * @return 响应
     */
    public Mono<ServerResponse> getApplies(ServerRequest request) {
        String userIdFromJWT = getUserId(request);
        String userId = request.pathVariable("id");

        if (!Objects.equals(userId, userIdFromJWT)) {
            log.info("\nIn Path: id = {}\nIn JWT:  id = {}", userId, userIdFromJWT);
            return ServerResponse.ok().bodyValue(Result.error(2, "用户 ID 不匹配"));
        }

        int offset = getOffset(request);
        int limit = getLimit(request);

        return PageUtils.pagingFlux(applyService.getAppliesByUserId(userId), offset, limit, Applies::toGetApplies)
                .flatMap(applies -> ServerResponse.ok().bodyValue(Result.success(applies, SUCCESS)));
    }

    public Mono<ServerResponse> getAllUnfinishedApplies(ServerRequest request) {
        int offset = getOffset(request);
        int limit = getLimit(request);

        return PageUtils.pagingFlux(applyService.getAllUnfinishedApplies(), offset, limit, Applies::toGetApplies)
                .flatMap(applies -> ServerResponse.ok().bodyValue(Result.success(applies, SUCCESS)));
    }

    // FIXME 接口尚未测试
    /**
     * 学生可以提前一天撤销初访申请记录
     *
     * @param request 请求
     * @return 响应
     */
    public Mono<ServerResponse> deleteApplyById(ServerRequest request) {
        String userIdFromJWT = getUserId(request);
        String userId = request.pathVariable("user_id");

        if (!Objects.equals(userId, userIdFromJWT)) {
            log.info("In Path: id = {}", userId);
            log.info("In JWT:  id = {}", userIdFromJWT);
            return Mono.just(Result.error(2, "用户 ID 不匹配"))
                    .doOnNext(result -> log.info("{}", result))
                    .flatMap(ServerResponse.ok()::bodyValue);
        }

        String applyId = request.pathVariable("global_id");

        return applyService.getApplyByApplyId(applyId)
                .flatMap(apply -> {
                    if (!Objects.equals(userId, apply.getUserId())) {
                        return Mono.just(Result.error(4, "用户无权查看此记录（用户 ID 与 Global ID 不匹配）"));
                    }

                    if (ZERO.equals(apply.getStatus())) {
                        return Mono.just(Result.error(5, "该初访预约申请尚未通过，无法撤销"));
                    }

                    if (TWO.equals(apply.getStatus())) {
                        return Mono.just(Result.error(6, "该初访预约申请已被拒绝，无法撤销"));
                    }

                    if (THREE.equals(apply.getStatus())) {
                        return Mono.just(Result.error(7, "该初访预约申请已被通过，无法撤销"));
                    }

                    if (!LocalDate.now().plusDays(1).isBefore(apply.getApplyPassTime())) {
                        return Mono.just(Result.error(1, "撤销失败（必须提前一天撤销）"));
                    }

                    return applyService.revokeApply(apply)
                            .map(it -> Result.success(0, "撤销成功"));
                })
                .switchIfEmpty(Mono.just(Result.error(3, "没有该初访预约申请")))
                .doOnNext(result -> log.info("{}", result))
                .flatMap(ServerResponse.ok()::bodyValue);
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
     * 获取请求参数中的 offset
     *
     * @return offset（默认值为 0）
     */
    private int getOffset(ServerRequest request) {
        String offset = request.exchange().getRequest().getQueryParams().getFirst("offset");

        if (Strings.isNullOrEmpty(offset)) {
            return 0;
        }

        return Integer.parseInt(offset);
    }

    /**
     * 获取请求参数中的 limit
     *
     * @return limit（默认值为 {@code Integer.MAX_VALUE}）
     */
    private int getLimit(ServerRequest request) {
        String limit = request.exchange().getRequest().getQueryParams().getFirst("limit");

        if (Strings.isNullOrEmpty(limit)) {
            return Integer.MAX_VALUE;
        }

        return Integer.parseInt(limit);
    }
}
