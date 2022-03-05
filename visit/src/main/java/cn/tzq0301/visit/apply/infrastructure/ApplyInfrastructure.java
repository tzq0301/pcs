package cn.tzq0301.visit.apply.infrastructure;

import cn.tzq0301.visit.apply.entity.Apply;
import cn.tzq0301.visit.apply.reposiroty.ApplyRepository;
import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * @author tzq0301
 * @version 1.0
 */
@Repository
@AllArgsConstructor
public class ApplyInfrastructure {
    private final ApplyRepository applyRepository;

    public Mono<Apply> saveApply(Apply apply) {
        return applyRepository.save(apply);
    }

    public Mono<Apply> getApplyByApplyId(String applyId) {
        return Mono.defer(() -> applyRepository.findById(new ObjectId(applyId)))
                .onErrorResume(IllegalArgumentException.class, e -> Mono.empty());
    }
}
