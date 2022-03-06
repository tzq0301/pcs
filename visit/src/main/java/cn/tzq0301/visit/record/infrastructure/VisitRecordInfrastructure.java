package cn.tzq0301.visit.record.infrastructure;

import cn.tzq0301.visit.record.entity.VisitRecord;
import cn.tzq0301.visit.record.repository.VisitRecordRepository;
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
public class VisitRecordInfrastructure {
    private final VisitRecordRepository visitRecordRepository;

    public Mono<VisitRecord> findVisitRecordById(String id) {
        return visitRecordRepository.findById(new ObjectId(id));
    }

    public Mono<VisitRecord> findVisitRecordById(ObjectId id) {
        return visitRecordRepository.findById(id);
    }
}
