package cn.tzq0301.duty.infrastructure;

import cn.tzq0301.duty.entity.duty.Duty;
import cn.tzq0301.duty.entity.work.Work;
import cn.tzq0301.duty.repository.DutyRepository;
import cn.tzq0301.duty.repository.WorkRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * @author tzq0301
 * @version 1.0
 */
@Repository
@AllArgsConstructor
public class DutyInfrastructure {
    private final DutyRepository dutyRepository;

    private final WorkRepository workRepository;

    public Mono<Duty> getDutyByUserId(String userId) {
        return dutyRepository.getByUserId(userId);
    }

    public Mono<Duty> saveDuty(Duty duty) {
        return dutyRepository.save(duty);
    }

    public Mono<Work> getWorkByUserId(String userId) {
        return workRepository.getByUserId(userId);
    }

    public Mono<Work> saveWork(Work work) {
        return workRepository.save(work);
    }
}
