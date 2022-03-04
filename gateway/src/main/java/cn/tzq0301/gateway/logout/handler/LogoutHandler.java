package cn.tzq0301.gateway.logout.handler;

import cn.tzq0301.gateway.logout.service.LogoutService;
import cn.tzq0301.util.JWTUtils;
import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * @author tzq0301
 * @version 1.0
 */
@Component
@AllArgsConstructor
public class LogoutHandler {

    private final LogoutService logoutService;

    public Mono<ServerResponse> logout(ServerRequest request) {
        String jwt = request.headers().firstHeader(AUTHORIZATION);

        if (Strings.isNullOrEmpty(jwt)) {
            return ServerResponse.ok().build();
        }

        jwt = jwt.substring(JWTUtils.AUTHORIZATION_HEADER_PREFIX.length());

        return logoutService.logout(jwt).flatMap(ServerResponse.ok()::bodyValue);
    }

}
