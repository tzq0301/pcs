package cn.tzq0301.gateway.route;

import cn.tzq0301.gateway.login.LoginHandler;
import cn.tzq0301.util.JWTUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * @author tzq0301
 * @version 1.0
 */
@SpringBootConfiguration
@AllArgsConstructor
@Log4j2
public class RouterConfig {
    private final LoginHandler loginHandler;

    @Bean
    public RouterFunction<ServerResponse> router() {
        return route()
                .GET("/test", request -> Mono.just("test").flatMap(ServerResponse.ok()::bodyValue))
                .GET("/test/user_id/{user_id}", request -> {
                    String userId = request.pathVariable("user_id");
                    String jwt = Objects.requireNonNull(
                            request.exchange().getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
                            .substring("Bearer ".length());
                    String userIdFromJWT = JWTUtils.extractUserId(jwt);
                    log.info("jwt            : {}", jwt);
                    log.info("userId         : {}", userId);
                    log.info("userId from JWT: {}", userIdFromJWT);
                    return ServerResponse.ok().build();
                })
                .GET("/login/account/{account}/password/{password}", loginHandler::loginByAccount)
                .build();
    }
}
