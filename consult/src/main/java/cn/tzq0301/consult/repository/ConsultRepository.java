package cn.tzq0301.consult.repository;

import cn.tzq0301.consult.entity.Consult;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

/**
 * @author tzq0301
 * @version 1.0
 */
public interface ConsultRepository extends ReactiveMongoRepository<Consult, ObjectId> {
    Flux<Consult> findAllByStudentId(final String studentId);
}
