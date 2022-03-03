package cn.tzq0301.auth.user.repository;

import cn.tzq0301.auth.entity.user.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

/**
 * @author tzq0301
 * @version 1.0
 */
public interface UserRepository extends ReactiveMongoRepository<User, String> {
    Mono<User> findByUserId(String userId);

    Mono<User> findByIdentity(String identity);

    Mono<User> findByPhone(String phone);
}
