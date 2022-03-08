package cn.tzq0301.consult.manager;

import cn.tzq0301.consult.entity.visit.VisitRecord;
import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * @author tzq0301
 * @version 1.0
 */
@Service
@AllArgsConstructor
public class ConsultManager {
    private final WebClient.Builder builder;

    public Mono<VisitRecord> findVisitRecordById(final ObjectId id) {
        return builder.build().get()
                .uri("lb://pcs-visit/global_id/{global_id}", id)
                .retrieve()
                .bodyToMono(VisitRecord.class);
    }
}
