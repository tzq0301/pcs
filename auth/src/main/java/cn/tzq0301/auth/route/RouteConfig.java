package cn.tzq0301.auth.route;

import cn.tzq0301.auth.login.LoginHandler;
import cn.tzq0301.auth.api.TestHandler;
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
public class RouteConfig {
    private final TestHandler testHandler;

    private final LoginHandler loginHandler;

    public RouteConfig(TestHandler testHandler, LoginHandler loginHandler) {
        this.testHandler = testHandler;
        this.loginHandler = loginHandler;
    }

    @Bean
    public RouterFunction<ServerResponse> router() {
        return route()
                .GET("/test", testHandler::test)
                .GET("/student", testHandler::student)
                .GET("/admin", testHandler::admin)
                .GET("/login/account/{account}/password/{password}", loginHandler::loginByAccount)
                .build();
    }
}
