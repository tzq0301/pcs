package cn.tzq0301.gateway.route;

import cn.tzq0301.gateway.login.LoginHandler;
import lombok.AllArgsConstructor;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * @author tzq0301
 * @version 1.0
 */
@SpringBootConfiguration
@AllArgsConstructor
public class RouterConfig {
    private final LoginHandler loginHandler;

    @Bean
    public RouterFunction<ServerResponse> router() {
        return route()
                .GET("/test", request -> Mono.just("test")
                        .flatMap(ServerResponse.ok()::bodyValue))
                .GET("/login/account/{account}/password/{password}", loginHandler::loginByAccount)
                .build();
    }
}
