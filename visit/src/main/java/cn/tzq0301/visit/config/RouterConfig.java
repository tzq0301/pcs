package cn.tzq0301.visit.config;

import cn.tzq0301.visit.apply.handler.ApplyHandler;
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

    @Bean
    public RouterFunction<ServerResponse> router() {
        return route()
                .POST("/apply", applyHandler::requestApply)
                .GET("/id/{id}/apply_id/{apply_id}", applyHandler::getApply)
                .GET("/id/{id}/applys", applyHandler::getApplies)
                .GET("/unfinished_applies", applyHandler::getAllUnfinishedApplies)
                .build();
    }
}
