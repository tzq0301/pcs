package cn.tzq0301.visit.record.manager;

import cn.tzq0301.util.DateUtils;
import cn.tzq0301.visit.apply.entity.UserInfo;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

/**
 * @author tzq0301
 * @version 1.0
 */
@Service
@AllArgsConstructor
public class VisitRecordManager {
    private final WebClient.Builder builder;

    public Mono<ResponseEntity<Void>> addWorkByUserId(final String userId, final LocalDate day, final Integer from, final String address) {
        return builder.build().post()
                .uri("lb://pcs-duty/user_id/{user_id}/day/{day}/from/{from}/address/{address}",
                        userId, DateUtils.localDateToString(day), from.toString(), address)
                .retrieve()
                .toBodilessEntity();
    }

    public Mono<String> deleteWorkByUserId(final String userId, final LocalDate day, final Integer from, final String address) {
        return builder.build().delete()
                .uri("lb://pcs-duty/user_id/{user_id}/day/{day}/from/{from}/address/{address}",
                        userId, DateUtils.localDateToString(day), from.toString(), address)
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<UserInfo> findUserInfoByUserId(String userId) {
        return builder.build().get()
                .uri("lb://pcs-auth/user_id/{user_id}/info", userId)
                .retrieve()
                .bodyToMono(UserInfo.class);
    }

    public Mono<Integer> setStudentStatus(String userId, int studentStatus) {
        return builder.build().patch()
                .uri("lb://pcs-auth/user_id/{user_id}/student_status/{student_status}", userId, studentStatus)
                .retrieve()
                .bodyToMono(Integer.class);
    }
}
