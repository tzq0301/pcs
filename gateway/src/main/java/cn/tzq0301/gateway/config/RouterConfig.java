package cn.tzq0301.gateway.config;

import cn.tzq0301.gateway.general.handler.GeneralHandler;
import cn.tzq0301.gateway.login.handler.LoginHandler;
import cn.tzq0301.gateway.logout.handler.LogoutHandler;
import cn.tzq0301.util.JWTUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.security.SignatureException;
import java.util.Objects;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
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

    private final LogoutHandler logoutHandler;

    private final GeneralHandler generalHandler;

    @Bean
    public RouterFunction<ServerResponse> router() {
        return route()
                // 测试接口
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
                .GET("/test/authorization", request -> Mono.justOrEmpty(
                                request.exchange().getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
                        .filter(authorizationHeader -> authorizationHeader.startsWith(JWTUtils.AUTHORIZATION_HEADER_PREFIX)
                                && authorizationHeader.length() > JWTUtils.AUTHORIZATION_HEADER_PREFIX.length())
                        .map(authorizationHeader -> JWTUtils.extractUserId(authorizationHeader.substring(JWTUtils.AUTHORIZATION_HEADER_PREFIX.length())))
                        .flatMap(it -> ServerResponse.ok().build())
                        .onErrorResume(
                                ex -> ex instanceof SignatureException
                                        || ex instanceof ExpiredJwtException
                                        || ex instanceof MalformedJwtException,
                                exception -> ServerResponse.status(HttpStatus.UNAUTHORIZED).build())
                        .switchIfEmpty(ServerResponse.status(HttpStatus.UNAUTHORIZED).build()))

                // 正式接口

                // 用户管理
                // 用户管理 - 用户登录
                .path("/login", this::loginRouter)
                // 用户管理 - 用户登出
                .GET("/logout", logoutHandler::logout)

                // 通用接口
                .GET("/phone/{phone}/code", generalHandler::sendCodeToPhone)

                .build();
    }

    private RouterFunction<ServerResponse> loginRouter() {
        return route()
                .nest(accept(APPLICATION_JSON), builder -> builder
                        .GET("/account/{account}/password/{password}", loginHandler::loginByAccount)
                        .GET("/phone/{phone}/code/{code}", loginHandler::loginByCode)
                        .build()
                ).build();
    }
}
