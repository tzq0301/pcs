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
                .GET("/user_id/{user_id}/duties", dutyHandler::findDutyByUserId)
                .GET("/user_id/{user_id}/works", dutyHandler::findWorkByUserId)
                .GET("/duties", dutyHandler::findAllDuties)
                .GET("/user_id/{user_id}/day/{day}/from/{from}", dutyHandler::findAddressByUserIdAndDayAndFrom)
                .POST("/user_id/{user_id}/day/{day}/from/{from}/address/{address}", dutyHandler::addWorkItem)
                .POST("/user_id/{user_id}/work/weekday/{weekday}/from/{from}/address/{address}", dutyHandler::addWorkItemAndReturn)
                .DELETE("/user_id/{user_id}/day/{day}/from/{from}/address/{address}", dutyHandler::deleteWorkItem)
                .POST("/user_id/{user_id}/duty/weekday/{weekday}/from/{from}/address/{address}", dutyHandler::addRegularDuty)
                .DELETE("/user_id/{user_id}/duty/weekday/{weekday}/from/{from}/address/{address}", dutyHandler::removeRegularDuty)
                .POST("/user_id/{user_id}/duty/day/{day}/from/{from}/address/{address}/type/{type}", dutyHandler::addWorkOvertimeRecord)
                .DELETE("/user_id/{user_id}/duty/day/{day}/from/{from}", dutyHandler::removeWorkOvertimeRecord)
                .POST("/user_id/{user_id}/work/weekday/{weekday}/from/{from}/address/{address}/times/{times}", dutyHandler::addWorkItemOfTimesForUser)
                .build();
    }
}
