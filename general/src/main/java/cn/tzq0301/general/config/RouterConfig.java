package cn.tzq0301.general.config;

import cn.tzq0301.general.address.handler.AddressHandler;
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
    private final AddressHandler addressHandler;

    @Bean
    public RouterFunction<ServerResponse> router() {
        return route()
                .GET("/addresses", addressHandler::listAddresses)
                .GET("/day/{day}/from/{from}/addresses", addressHandler::listAvailableAddressesByDay)
                .GET("/weekday/{weekday}/from/{from}/addresses", addressHandler::listAvailableAddressesByWeekday)
                .build();
    }
}
