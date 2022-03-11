package cn.tzq0301.auth.user.manager;

import cn.tzq0301.auth.config.RedisConfig;
import cn.tzq0301.auth.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * @author tzq0301
 * @version 1.0
 */
@Service
@AllArgsConstructor
@Log4j2
public class UserManager {
    private final ReactiveRedisTemplate<String, Object> redisTemplate;

    private static final String USER_HASH_KEY = RedisConfig.USER_NAMESPACE_PREFIX + "user";

    public Mono<User> putUserIdAndUserIntoCache(final Mono<User> user) {
        return user
                .flatMap(it -> redisTemplate.opsForHash().put(USER_HASH_KEY, it.getUserId(), it))
                .flatMap(it -> user)
                .doOnNext(it -> log.info("Put User into Cache -> {}", it));
    }

    public Mono<User> putUserIdAndUserIntoCache(final User user) {
        return redisTemplate.opsForHash().put(USER_HASH_KEY, user.getUserId(), user)
                .map(it -> user)
                .doOnNext(it -> log.info("Put User into Cache -> {}", it));
    }

    public Mono<User> getUserByUserIdFromCache(final String userId) {
        return redisTemplate.opsForHash().get(USER_HASH_KEY, userId)
                .map(obj -> (User) obj)
                .doOnNext(it -> log.info("Got User from Cache -> {}", it))
                .onErrorResume(ex -> ex instanceof Exception, Mono::error);
    }
}
