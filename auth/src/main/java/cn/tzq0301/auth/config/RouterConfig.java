package cn.tzq0301.auth.config;

import cn.tzq0301.auth.api.TestHandler;
import cn.tzq0301.auth.login.handler.LoginHandler;
import cn.tzq0301.auth.user.handler.UserHandler;
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
public class RouterConfig {
    private final TestHandler testHandler;

    private final LoginHandler loginHandler;

    private final UserHandler userHandler;

    @Bean
    public RouterFunction<ServerResponse> router() {
        return route()
                .GET("/test", testHandler::test)
                .GET("/student", testHandler::student)
                .GET("/admin", testHandler::admin)
                .GET("/account/{account}", loginHandler::getUserByAccount)
                .GET("/phone/{phone}", userHandler::isPhoneInEnduranceContainer)
                .GET("/user_id/{user_id}", userHandler::getUserInformation)
                .GET("/user_id/{user_id}/info", userHandler::getUserInfo)
                .PATCH("/user_id/{user_id}", userHandler::updateUserInformation)
                .PATCH("/user_id/{user_id}/old_password/{old_password}/new_password/{new_password}", userHandler::updatePassword)
                .GET("/user_id/{user_id}/student_status", userHandler::isUserAbleToApply)
                .PATCH("/user_id/{user_id}/student_status/{student_status}", userHandler::setStudentStatusToOne)
                .build();
    }
}
