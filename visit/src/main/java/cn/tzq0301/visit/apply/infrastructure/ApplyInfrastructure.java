package cn.tzq0301.visit.apply.infrastructure;

import cn.tzq0301.visit.apply.entity.Apply;
import cn.tzq0301.visit.apply.reposiroty.ApplyRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * @author tzq0301
 * @version 1.0
 */
@Repository
@AllArgsConstructor
@Log4j2
public class ApplyInfrastructure {
    private final ApplyRepository applyRepository;

    public Mono<Apply> saveApply(final Apply apply) {
        return applyRepository.save(apply)
                .doOnNext(it -> log.info("Save apply: {}", apply));
    }

    public Mono<Apply> findApplyByApplyId(final String applyId) {
        return Mono.defer(() -> applyRepository.findById(new ObjectId(applyId)))
                .onErrorResume(IllegalArgumentException.class, e -> Mono.empty());
    }

    public Flux<Apply> listAppliesByUserId(final String userId) {
        return applyRepository.findByUserId(userId);
    }

    public Flux<Apply> listAllAppliesByStatus(final Integer status) {
        return applyRepository.findAll()
                .filter(apply -> Objects.equals(status, apply.getStatus()));
    }

    public Flux<Apply> listAllApplies() {
        return applyRepository.findAll();
    }

    public Mono<Void> deleteApplyById(final String id) {
        return applyRepository.deleteById(new ObjectId(id));
    }

    public Mono<Void> deleteApplyById(final ObjectId id) {
        return applyRepository.deleteById(id);
    }
}
