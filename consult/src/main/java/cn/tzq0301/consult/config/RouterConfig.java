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
                .POST("/global_id/{global_id}/weekday/{weekday}/from/{from}/address/{address}/consultor_id/{consultor_id}", consultHandler::generateConsult)
                .GET("/user_id/{user_id}/consult_records", consultHandler::listStudentConsultByStudentId)
                .GET("/user_id/{user_id}/global_id/{global_id}/consult/records", consultHandler::findStudentConsultDetailByGlobalId)
                .GET("/records", consultHandler::listAllConsultRecordsForAssistant)
                .GET("/consultor_id/{consultor_id}/records", consultHandler::listAllConsultRecordsOfConsultorByConsultorId)
                .GET("/consultor_id/{consultor_id}/record/global_id/{global_id}", consultHandler::findConsultRecordForConsultorByGlobalId)
                .POST("/global_id/{global_id}", consultHandler::commitRecordByGlobalId)
                .POST("/global_id/{global_id}/finish", consultHandler::finishConsultByGlobalId)
                .GET("/statics_infos", consultHandler::listAllStaticsInfos)
                .GET("/pdf_info/global_id/{global_id}", consultHandler::findPdfInfoByGlobalId)
                .GET("/consultor_id/{consultor_id}/statics_info", consultHandler::findStaticsInfoByConsultorId)
                .build();
    }
}
