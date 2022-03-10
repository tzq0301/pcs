package cn.tzq0301.statics.config;

import cn.tzq0301.statics.handler.StaticsHandler;
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
    private final StaticsHandler staticsHandler;

    @Bean
    public RouterFunction<ServerResponse> router() {
        return route()
                .GET("/infos", staticsHandler::listInfos)
                .POST("/reports/pdf", staticsHandler::exportConsultReports)
                .POST("/reports/pdf/zip", staticsHandler::exportConsultReportsByZip)
                .GET("/global_id/{global_id}/info/pdf", staticsHandler::exportConsultReportByGlobalId)
                .GET("/consultors", staticsHandler::exportConsultorInfos)
                .build();
    }
}
