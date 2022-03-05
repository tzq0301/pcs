package cn.tzq0301.visit.apply.manager;

import cn.tzq0301.util.JWTUtils;
import cn.tzq0301.visit.apply.entity.UserInfo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static com.google.common.net.HttpHeaders.AUTHORIZATION;

/**
 * @author tzq0301
 * @version 1.0
 */
@Service
@AllArgsConstructor
public class ApplyManager {
    private final WebClient.Builder builder;

    public Mono<Boolean> isUserAbleToApply(String userId) {
        return builder.build().get()
                .uri("lb://pcs-auth/user_id/{user_id}/student_status", userId)
                .retrieve()
                .bodyToMono(Integer.class)
                .map(value -> new Integer(0).equals(value) ? Boolean.TRUE : Boolean.FALSE);
    }

    public Mono<Integer> setStudentStatus(String userId, int studentStatus) {
        return builder.build().patch()
                .uri("lb://pcs-auth/user_id/{user_id}/student_status/{student_status}", userId, studentStatus)
                .retrieve()
                .bodyToMono(Integer.class);
    }

    public Mono<UserInfo> findUserInfoByJWT(String jwt) {
        return builder.build().get()
                .uri("lb://pcs-auth/user_id/{user_id}/info", JWTUtils.extractUserId(jwt))
                .header(AUTHORIZATION, JWTUtils.AUTHORIZATION_HEADER_PREFIX + jwt)
                .retrieve()
                .bodyToMono(UserInfo.class);
    }
}
