package cn.tzq0301.visit.record.service;

import cn.tzq0301.visit.record.entity.VisitRecord;
import cn.tzq0301.visit.record.infrastructure.VisitRecordInfrastructure;
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
public class VisitRecordService {
    private final VisitRecordInfrastructure visitRecordInfrastructure;

    public Mono<VisitRecord> findVisitRecordById(String id) {
        return visitRecordInfrastructure.findVisitRecordById(id);
    }

    public Mono<VisitRecord> findVisitRecordById(ObjectId id) {
        return visitRecordInfrastructure.findVisitRecordById(id);
    }
}
