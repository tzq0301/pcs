package cn.tzq0301.duty.repository;

import cn.tzq0301.duty.entity.duty.Duty;
import cn.tzq0301.duty.entity.duty.Pattern;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

/**
 * @author tzq0301
 * @version 1.0
 */
public interface DutyRepository extends ReactiveMongoRepository<Duty, ObjectId> {
    Mono<Duty> getByUserId(String userId);
}
