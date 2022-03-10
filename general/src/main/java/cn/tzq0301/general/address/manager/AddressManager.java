package cn.tzq0301.general.address.manager;

import cn.tzq0301.general.config.RedisConfig;
import cn.tzq0301.util.DateUtils;
import lombok.AllArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
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

    public Mono<List<String>> listNonSpareAddressByDay(final LocalDate day, final int from) {
        return builder.build().get()
                .uri("lb://pcs-duty/day/{day}/from/{from}/non_spare_addresses",
                        DateUtils.localDateToString(day), from)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {
                });
    }
}
