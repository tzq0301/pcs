package cn.tzq0301.visit.apply.handler;

import cn.tzq0301.result.Result;
import cn.tzq0301.util.JWTUtils;
import cn.tzq0301.visit.apply.entity.vo.ApplyRequest;
import cn.tzq0301.visit.apply.entity.vo.ApplyRequestException;
import cn.tzq0301.visit.apply.entity.vo.ApplyRequestResult;
import cn.tzq0301.visit.apply.service.ApplyService;
import lombok.AllArgsConstructor;
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
public class ApplyHandler {
    private final ApplyService applyService;

    public Mono<ServerResponse> requestApply(ServerRequest request) {
        String userId = Objects.requireNonNull(request.headers().firstHeader(AUTHORIZATION))
                .substring(JWTUtils.AUTHORIZATION_HEADER_PREFIX.length());

        return applyService.requestApply(request.bodyToMono(ApplyRequest.class))
                .flatMap(apply -> ServerResponse.created(
                        URI.create("/visit/id/" + userId + "/apply_id/" + apply.getId())).build())
                .onErrorResume(ApplyRequestException.class, error ->
                        ServerResponse.ok().bodyValue(Result.error(ApplyRequestResult.NO_NEED_TO_APPLY_TWICE)));
    }
}
