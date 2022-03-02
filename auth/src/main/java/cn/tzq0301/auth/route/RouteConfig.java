package cn.tzq0301.auth.route;

import cn.tzq0301.auth.login.LoginHandler;
import cn.tzq0301.auth.api.TestHandler;
import cn.tzq0301.auth.user.UserHandler;
import lombok.AllArgsConstructor;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * @author tzq0301
 * @version 1.0
 */
@SpringBootConfiguration
@AllArgsConstructor
public class RouteConfig {
    private final TestHandler testHandler;

    private final LoginHandler loginHandler;

    private final UserHandler userHandler;

    @Bean
    public RouterFunction<ServerResponse> router() {
        return route()
                .GET("/test", testHandler::test)
                .GET("/student", testHandler::student)
                .GET("/admin", testHandler::admin)
                .GET("/login/account/{account}/password/{password}", loginHandler::loginByAccount)
                .GET("/account/{account}", userHandler::getUserByAccount)
                .build();
    }
}
