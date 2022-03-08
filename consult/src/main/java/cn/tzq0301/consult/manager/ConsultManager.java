package cn.tzq0301.consult.manager;

import cn.tzq0301.consult.entity.UserInfo;
import cn.tzq0301.consult.entity.visit.VisitRecord;
import cn.tzq0301.consult.entity.work.WorkArrange;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

/**
 * @author tzq0301
 * @version 1.0
 */
@Service
@AllArgsConstructor
@Log4j2
public class ConsultManager {
    private final WebClient.Builder builder;

    public Mono<VisitRecord> findVisitRecordById(final ObjectId id) {
        log.info("Global ID -> {}", id);
        return builder.build().get()
                .uri("lb://pcs-visit/global_id/{global_id}", id.toHexString())
                .retrieve()
                .bodyToMono(VisitRecord.class)
                .doOnNext(visitRecord -> log.info("Got VisitRecord -> {}", visitRecord));
    }

    public Mono<UserInfo> findUserInfoByUserId(String userId) {
        return builder.build().get()
                .uri("lb://pcs-auth/user_id/{user_id}/info", userId)
                .retrieve()
                .bodyToMono(UserInfo.class);
    }

    public Flux<LocalDate> addWorkItemsForUser(final String userId, final int weekday, final int from,
                                               final String address, final int times) {
        return builder.build().post()
                .uri("lb://pcs-duty/user_id/{user_id}/work/weekday/{weekday}/from/{from}/address/{address}/times/{times}",
                        userId, weekday, from, address, times)
                .retrieve()
                .bodyToFlux(LocalDate.class);
    }

    public Mono<String> passVisitRecord(final String id) {
        return builder.build().patch()
                .uri("lb://pcs-visit/record/global_id/{global_id}/arrange", id)
                .retrieve()
                .bodyToMono(String.class);
    }
}
