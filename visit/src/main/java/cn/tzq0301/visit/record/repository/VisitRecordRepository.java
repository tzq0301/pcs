package cn.tzq0301.visit.record.repository;

import cn.tzq0301.visit.record.entity.VisitRecord;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * @author tzq0301
 * @version 1.0
 */
public interface VisitRecordRepository extends ReactiveMongoRepository<VisitRecord, ObjectId> {
}
