package cn.tzq0301.statics.config;

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
public class RouterConfig {
    @Bean
    public RouterFunction<ServerResponse> router() {
        return route()
                .build();
    }
}
