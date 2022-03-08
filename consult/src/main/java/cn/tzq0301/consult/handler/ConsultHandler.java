package cn.tzq0301.consult.handler;

import cn.tzq0301.consult.service.ConsultService;
import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * @author tzq0301
 * @version 1.0
 */
@Component
@AllArgsConstructor
public class ConsultHandler {
    private final ConsultService consultService;

    public Mono<ServerResponse> generateConsult(ServerRequest request) {
        // FIXME
        int weekday = Integer.parseInt(request.pathVariable("weekday"));
        int from = Integer.parseInt(request.pathVariable("from"));
        String address = request.pathVariable("address");

        return consultService.generateConsult(new ObjectId(request.pathVariable("global_id")),
                        weekday, from, address)
                .flatMap(ServerResponse.ok()::bodyValue);
    }
}
