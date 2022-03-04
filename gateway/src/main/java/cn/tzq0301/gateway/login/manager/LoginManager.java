package cn.tzq0301.gateway.login.manager;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static cn.tzq0301.gateway.config.RedisConfig.SMS_NAMESPACE_PREFIX;

/**
 * @author tzq0301
 * @version 1.0
 */
@Service
@AllArgsConstructor
@Log4j2
public class LoginManager {
    private final ReactiveRedisTemplate<String, Object> redisTemplate;

    public Mono<Boolean> isValidationCodeConsistentWithPhone(final String phone, final String code) {
        return redisTemplate.opsForValue().get(SMS_NAMESPACE_PREFIX + phone)
                .map(value -> (String) value)
                .map(value -> {
                    log.info("Got code {} from Redis, and code from user is {}", value, code);
                    return Objects.equals(code, value) ? Boolean.TRUE : Boolean.FALSE;
                });
    }
}
