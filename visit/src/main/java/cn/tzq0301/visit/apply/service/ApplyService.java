package cn.tzq0301.visit.apply.service;

import cn.tzq0301.visit.apply.entity.Applies;
import cn.tzq0301.visit.apply.entity.Apply;
import cn.tzq0301.visit.apply.entity.vo.ApplyRequest;
import cn.tzq0301.visit.apply.entity.vo.ApplyRequestException;
import cn.tzq0301.visit.apply.infrastructure.ApplyInfrastructure;
import cn.tzq0301.visit.apply.manager.ApplyManager;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static cn.tzq0301.util.Num.ONE;

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

    public Mono<Apply> requestApply(Mono<ApplyRequest> applyRequest) {
        return applyRequest
                .filterWhen(request -> applyManager.isUserAbleToApply(request.getId()).doOnNext(able -> {
                    if (!able) {
                        log.warn("{} is not able to create a new apply", request.getId());
                    }
                }))
                .flatMap(request -> applyInfrastructure.saveApply(Applies.newApply(
                        request.getId(), request.getPhone(), request.getEmail(),
                        request.getProblemId(), request.getProblemDetail(),request.getDay(),
                        request.getFrom(), request.getAddress(), request.getScores())))
                .flatMap(apply ->  applyManager.setStudentStatus(apply.getUserId(), 1)
                        .flatMap(it -> ONE.equals(it) ? Mono.just(apply) : Mono.error(ApplyRequestException::new)))
                .switchIfEmpty(Mono.error(ApplyRequestException::new));
    }
}
