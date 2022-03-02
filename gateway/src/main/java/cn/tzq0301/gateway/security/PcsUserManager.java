package cn.tzq0301.gateway.security;

import cn.tzq0301.gateway.security.entity.UserResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * @author tzq0301
 * @version 1.0
 */
@Service
@AllArgsConstructor
public class PcsUserManager {
    private final WebClient.Builder builder;

    public Mono<UserResponse> getUserByUserId(String account) {
        return builder.build().get()
                .uri("http://pcs-auth/account/{account}", account)
                .retrieve()
                .bodyToMono(UserResponse.class);
    }
}
