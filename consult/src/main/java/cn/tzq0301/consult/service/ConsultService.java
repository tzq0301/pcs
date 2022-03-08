package cn.tzq0301.consult.service;

import cn.tzq0301.consult.infrastructure.ConsultInfrastructure;
import cn.tzq0301.consult.manager.ConsultManager;
import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * @author tzq0301
 * @version 1.0
 */
@Service
@AllArgsConstructor
public class ConsultService {
    private final ConsultManager consultManager;

    private final ConsultInfrastructure consultInfrastructure;

    public Mono<?> generateConsult(final ObjectId id, int weekday, int from, String address) {
        return consultManager.findVisitRecordById(id)
                .doOnNext(System.out::println);
    }
}
