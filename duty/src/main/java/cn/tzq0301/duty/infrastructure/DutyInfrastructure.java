package cn.tzq0301.duty.infrastructure;

import cn.tzq0301.duty.entity.duty.Duties;
import cn.tzq0301.duty.entity.duty.Duty;
import cn.tzq0301.duty.entity.work.Work;
import cn.tzq0301.duty.entity.work.Works;
import cn.tzq0301.duty.repository.DutyRepository;
import cn.tzq0301.duty.repository.WorkRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author tzq0301
 * @version 1.0
 */
@Repository
@AllArgsConstructor
@Log4j2
public class DutyInfrastructure {
    private final DutyRepository dutyRepository;

    private final WorkRepository workRepository;

    public Mono<Duty> getDutyByUserId(String userId) {
        return dutyRepository.getByUserId(userId)
                .switchIfEmpty(this.initDuty(userId));
    }

    public Mono<Duty> saveDuty(Duty duty) {
        return dutyRepository.save(duty);
    }

    public Flux<Duty> findAllDuties() {
        return dutyRepository.findAll();
    }

    public Flux<Work> findAllWorks() {
        return workRepository.findAll();
    }

    public Mono<Work> getWorkByUserId(String userId) {
        return workRepository.getByUserId(userId)
                .switchIfEmpty(this.initWork(userId));
    }

    public Mono<Work> saveWork(Work work) {
        log.info("Save Work -> {}", work);
        return workRepository.save(work);
    }

    public Mono<Duty> initDuty(final String userId) {
        return dutyRepository.save(Duties.newDuty(userId));
    }

    public Mono<Work> initWork(final String userId) {
        return workRepository.save(Works.newWork(userId));
    }
}
