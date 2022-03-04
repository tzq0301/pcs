package cn.tzq0301.gateway.logout.manager;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * @author tzq0301
 * @version 1.0
 */
@Service
@AllArgsConstructor
public class LogoutManager {
    private final ReactiveRedisTemplate<String, Object> redisTemplate;

    public Mono<Void> logout(final String jwt) {
        return redisTemplate.opsForValue().delete(jwt).then();
    }
}
