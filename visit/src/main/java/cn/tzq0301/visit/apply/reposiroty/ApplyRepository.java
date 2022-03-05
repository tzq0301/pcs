package cn.tzq0301.visit.apply.reposiroty;

import cn.tzq0301.visit.apply.entity.Apply;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

/**
 * @author tzq0301
 * @version 1.0
 */
public interface ApplyRepository extends ReactiveMongoRepository<Apply, ObjectId> {
    Flux<Apply> findByUserId(final String userId);
}
