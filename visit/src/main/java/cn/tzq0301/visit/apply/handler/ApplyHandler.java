package cn.tzq0301.visit.apply.handler;

import cn.tzq0301.result.Result;
import cn.tzq0301.util.DateUtils;
import cn.tzq0301.util.JWTUtils;
import cn.tzq0301.visit.apply.entity.applyrequest.ApplyRequest;
import cn.tzq0301.visit.apply.entity.applyrequest.ApplyRequestException;
import cn.tzq0301.visit.apply.entity.applyrequest.ApplyRequestResult;
import cn.tzq0301.visit.apply.entity.getapply.GetApply;
import cn.tzq0301.visit.apply.entity.getapply.GetApplyResult;
import cn.tzq0301.visit.apply.service.ApplyService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Objects;

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
}
