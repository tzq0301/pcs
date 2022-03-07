package cn.tzq0301.visit.record.infrastructure;

import cn.tzq0301.visit.record.entity.VisitRecord;
import cn.tzq0301.visit.record.repository.VisitRecordRepository;
import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author tzq0301
 * @version 1.0
 */
@Repository
@AllArgsConstructor
public class VisitRecordInfrastructure {
    private final VisitRecordRepository visitRecordRepository;

    public Mono<VisitRecord> findVisitRecordById(final String id) {
        return visitRecordRepository.findById(new ObjectId(id));
    }

    public Mono<VisitRecord> findVisitRecordById(final ObjectId id) {
        return visitRecordRepository.findById(id);
    }

    public Mono<VisitRecord> saveVisitRecord(final VisitRecord visitRecord) {
        return visitRecordRepository.save(visitRecord);
    }

    public Mono<String> deleteById(final ObjectId visitRecordId) {
        return visitRecordRepository.deleteById(visitRecordId).map(it -> "OK");
    }

    public Flux<VisitRecord> findVisitRecordByVisitorId(final String visitorId) {
        return visitRecordRepository.findVisitRecordByVisitorIdEquals(visitorId);
    }
}
