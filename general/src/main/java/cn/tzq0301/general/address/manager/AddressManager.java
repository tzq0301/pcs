package cn.tzq0301.general.address.manager;

import cn.tzq0301.general.config.RedisConfig;
import lombok.AllArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author tzq0301
 * @version 1.0
 */
@Service
@AllArgsConstructor
public class AddressManager {
    private final ReactiveRedisTemplate<String, Object> redisTemplate;

    private final WebClient.Builder builder;

    public Flux<String> listAddress() {
        return redisTemplate.opsForSet()
                .members(RedisConfig.ADDRESSES_KEY)
                .map(value -> (String) value);
    }

    public Mono<List<String>> listNonSpareAddressByWeekday(final int weekday, final int from) {
        return builder.build().get()
                .uri("lb://pcs-duty/weekday/{weekday}/from/{from}/non_spare_addresses", weekday, from)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {
                });
    }
}
