package cn.tzq0301.general.address.manager;

import cn.tzq0301.general.config.RedisConfig;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/**
 * @author tzq0301
 * @version 1.0
 */
@Service
@AllArgsConstructor
public class AddressManager {
    private final ReactiveRedisTemplate<String, Object> redisTemplate;

    public Flux<String> listAddress() {
        return redisTemplate.opsForSet()
                .members(RedisConfig.ADDRESSES_KEY)
                .map(value -> (String) value);
    }
}
