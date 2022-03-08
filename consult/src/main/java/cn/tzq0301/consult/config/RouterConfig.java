package cn.tzq0301.consult.config;

import cn.tzq0301.consult.handler.ConsultHandler;
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
    private final ConsultHandler consultHandler;

    @Bean
    public RouterFunction<ServerResponse> router() {
        return route()
                .POST("/global_id/{global_id}/weekday/{weekday}/from/{from}/address/{address}", consultHandler::generateConsult)
                .build();
    }
}
