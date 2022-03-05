package cn.tzq0301.visit.apply.handler;

import cn.tzq0301.result.Result;
import cn.tzq0301.util.DateUtils;
import cn.tzq0301.util.JWTUtils;
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
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

import static cn.tzq0301.result.DefaultResultEnum.SUCCESS;
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

    public Mono<ServerResponse> getApplies(ServerRequest request) {
        String userIdFromJWT = getUserId(request);
        String userId = request.pathVariable("id");

        if (!Objects.equals(userId, userIdFromJWT)) {
            log.info("\nIn Path: id = {}\nIn JWT:  id = {}", userId, userIdFromJWT);
            return ServerResponse.ok().bodyValue(Result.error(2, "用户 ID 不匹配"));
        }

        int offset = getOffset(request);
        int limit = getLimit(request);

        return applyService.getAppliesByUserId(userId)
                .collectList()
                .map(applies -> applies.subList(
                        Math.min(offset * limit, applies.size()),
                        Math.min((offset + 1) * limit, applies.size())))
//                .onErrorResume(
//                        ex -> ex instanceof IndexOutOfBoundsException || ex instanceof IllegalArgumentException,
//                        ex -> Mono.just(Lists.newArrayList()))
                .map(list -> list.stream().map(Applies::toGetApplies).collect(Collectors.toList()))
                .flatMap(applies -> ServerResponse.ok().bodyValue(Result.success(applies, SUCCESS)));
    }

    public Mono<ServerResponse> getAllUnfinishedApplies(ServerRequest request) {
        return null;
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
