package cn.tzq0301.auth.route;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * @author tzq0301
 * @version 1.0
 */
@SpringBootConfiguration
public class RouteConfig {
    @Bean
    public RouterFunction<ServerResponse> router() {
        return route()
                .GET("/test", request -> ServerResponse.ok().contentType(APPLICATION_JSON).bodyValue("test")).build();
    }
}
