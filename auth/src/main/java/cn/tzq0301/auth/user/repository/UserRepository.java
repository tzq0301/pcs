package cn.tzq0301.auth.user.repository;

import cn.tzq0301.auth.user.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

/**
 * @author tzq0301
 * @version 1.0
 */
public interface UserRepository extends ReactiveMongoRepository<User, ObjectId> {
    Mono<User> findByUserId(String userId);

    Mono<User> findByIdentity(String identity);

    Mono<User> findByPhone(String phone);
}
