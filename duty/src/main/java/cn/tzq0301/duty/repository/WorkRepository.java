package cn.tzq0301.duty.repository;

import cn.tzq0301.duty.entity.work.Work;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

/**
 * @author tzq0301
 * @version 1.0
 */
public interface WorkRepository extends ReactiveMongoRepository<Work, ObjectId> {
    Mono<Work> getByUserId(String userId);
}
