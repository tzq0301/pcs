package cn.tzq0301.visit.apply.service;

import cn.tzq0301.visit.apply.entity.Applies;
import cn.tzq0301.visit.apply.entity.Apply;
import cn.tzq0301.visit.apply.entity.applyrequest.ApplyRequest;
import cn.tzq0301.visit.apply.entity.applyrequest.ApplyRequestException;
import cn.tzq0301.visit.apply.infrastructure.ApplyInfrastructure;
import cn.tzq0301.visit.apply.manager.ApplyManager;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static cn.tzq0301.util.Num.ONE;
import static cn.tzq0301.util.Num.ZERO;

/**
 * @author tzq0301
 * @version 1.0
 */
@Service
@AllArgsConstructor
@Log4j2
public class ApplyService {
    private final ApplyManager applyManager;

    private final ApplyInfrastructure applyInfrastructure;

    public Mono<Apply> requestApply(String jwt, Mono<ApplyRequest> applyRequest) {
        return applyManager.findUserInfoByJWT(jwt)
                .flatMap(user -> {
                    log.info("Got user {}", user);

                    if (!ZERO.equals(user.getStudentStatus())) {
                        log.warn("{} is not able to create a new apply", user.getUserId());
                        return Mono.empty();
                    }

                    return Mono.just(user);
                })
                .flatMap(user -> applyRequest.map(request -> Applies.newApply(user, request))
                        .flatMap(apply ->
                                applyInfrastructure.saveApply(apply)
                                        .flatMap(it -> applyManager.setStudentStatus(it.getUserId(), ONE)
                                                .flatMap(status -> ONE.equals(status)
                                                        ? Mono.just(apply)
                                                        : Mono.error(ApplyRequestException::new)))))
                .switchIfEmpty(Mono.error(ApplyRequestException::new));
    }

    public Mono<Apply> getApplyByApplyId(String applyId) {
        return applyInfrastructure.getApplyByApplyId(applyId);
    }
}
