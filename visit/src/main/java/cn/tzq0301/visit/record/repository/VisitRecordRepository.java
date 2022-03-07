package cn.tzq0301.visit.record.repository;

import cn.tzq0301.visit.record.entity.VisitRecord;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author tzq0301
 * @version 1.0
 */
public interface VisitRecordRepository extends ReactiveMongoRepository<VisitRecord, ObjectId> {
    Flux<VisitRecord> findVisitRecordByVisitorIdEquals(final String visitorId);
}
