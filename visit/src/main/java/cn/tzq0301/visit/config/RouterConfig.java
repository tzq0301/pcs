package cn.tzq0301.visit.config;

import cn.tzq0301.visit.apply.handler.ApplyHandler;
import cn.tzq0301.visit.record.handler.VisitHandler;
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
    private final ApplyHandler applyHandler;

    private final VisitHandler visitHandler;

    @Bean
    public RouterFunction<ServerResponse> router() {
        return route()
                .POST("/apply", applyHandler::requestApply)
                .GET("/id/{id}/apply_id/{apply_id}", applyHandler::getApply)
                .GET("/id/{id}/applys", applyHandler::getApplies)
                .GET("/unfinished_applies", applyHandler::getAllUnfinishedApplies)
                .DELETE("/user_id/{user_id}/global_id/{global_id}", applyHandler::deleteApplyById)
                .POST("/pass_apply", applyHandler::passApply)
                .GET("/visitor_id/{visitor_id}/first_visit_records", visitHandler::listVisitRecordsByVisitorId)
                .GET("/user_id/{user_id}/global_id/{global_id}", visitHandler::listSpecificVisitRecord)
                .POST("/apply/global_id/{global_id}", visitHandler::submit)
                .GET("/applys", visitHandler::unhandledApplies)
                .GET("/global_id/{global_id}", visitHandler::findVisitRecordById)
                .GET("/first_records", applyHandler::getAllApplies)
                .DELETE("/reject/apply_id/{apply_id}", applyHandler::rejectApply)
                .DELETE("/records/global_id/{global_id}", applyHandler::deleteApplyByGlobalId)
                .build();
    }
}
