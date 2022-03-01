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

    public Mono<UserResponse> getUserByUserId(String userId) {
        return builder.build().get()
                .uri("http://pcs-auth/user_id/{user_id}", userId)
                .retrieve()
                .bodyToMono(UserResponse.class);
    }
}
