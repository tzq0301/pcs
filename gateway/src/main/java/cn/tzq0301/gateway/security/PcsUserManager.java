package cn.tzq0301.gateway.security;

import cn.tzq0301.gateway.security.entity.UserResponse;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * @author tzq0301
 * @version 1.0
 */
@Service
@AllArgsConstructor
@Log4j2
public class PcsUserManager {
    private final WebClient.Builder builder;

    public Mono<UserResponse> getUserByUserId(String account) {
        return builder.build().get()
                .uri("lb://pcs-auth/account/{account}", account)
                .retrieve()
                .bodyToMono(UserResponse.class);
    }

    public Mono<Boolean> isPhoneInEnduranceContainer(final String phone) {
        return builder.build().get()
                .uri("lb://pcs-auth/phone/{phone}", phone)
                .retrieve()
                .bodyToMono(Boolean.class)
                // Fxxk WebClient
                // If the result is false, it will let it be Mono.empty()
                // (but I really don't know why and wonder why and be crazy)
                // So I add the next line of `switchIfEmpty(...)`
                .switchIfEmpty(Mono.just(Boolean.FALSE))
                .doOnNext(value -> log.info("{} is{} valid", phone, value ? "" : " not"));
    }
}
