package cn.tzq0301.duty.manager;

import cn.tzq0301.duty.entity.UserInfo;
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
public class DutyManager {
    private final WebClient.Builder builder;

    public Mono<UserInfo> findUserInfoByUserId(String userId) {
        return builder.build().get()
                .uri("lb://pcs-auth/user_id/{user_id}/info", userId)
                .retrieve()
                .bodyToMono(UserInfo.class);
    }
}
