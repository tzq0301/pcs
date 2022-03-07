package cn.tzq0301.duty.config;

import cn.tzq0301.duty.handler.DutyHandler;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * @author tzq0301
 * @version 1.0
 */
@Component
@AllArgsConstructor
public class RouterConfig {
    private final DutyHandler dutyHandler;

    @Bean
    public RouterFunction<ServerResponse> router() {
        return route()
                .GET("/user_id/{user_id}/day/{day}/from/{from}", dutyHandler::findAddressByUserIdAndDayAndFrom)
                .POST("/user_id/{user_id}/day/{day}/from/{from}/address/{address}", dutyHandler::addWorkItem)
                .DELETE("/user_id/{user_id}/day/{day}/from/{from}/address/{address}", dutyHandler::deleteWorkItem)
                .build();
    }
}
