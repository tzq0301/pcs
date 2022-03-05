package cn.tzq0301.visit.apply.infrastructure;

import cn.tzq0301.visit.apply.reposiroty.ApplyRepository;
import cn.tzq0301.visit.apply.entity.Apply;
import lombok.AllArgsConstructor;
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
}
