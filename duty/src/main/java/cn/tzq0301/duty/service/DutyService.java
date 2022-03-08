package cn.tzq0301.duty.service;

import cn.tzq0301.duty.entity.duty.Duties;
import cn.tzq0301.duty.entity.duty.Duty;
import cn.tzq0301.duty.entity.duty.Pattern;
import cn.tzq0301.duty.entity.duty.SpecialItem;
import cn.tzq0301.duty.entity.duty.vo.AllDutyDetails;
import cn.tzq0301.duty.entity.duty.vo.DutyDetail;
import cn.tzq0301.duty.entity.work.Work;
import cn.tzq0301.duty.entity.work.WorkItem;
import cn.tzq0301.duty.entity.work.Works;
import cn.tzq0301.duty.infrastructure.DutyInfrastructure;
import cn.tzq0301.duty.manager.DutyManager;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author tzq0301
 * @version 1.0
 */
@Service
@AllArgsConstructor
@Log4j2
public class DutyService {
    private final DutyInfrastructure dutyInfrastructure;

    private final DutyManager dutyManager;

    public Mono<Duty> findDutyByUserId(String userId) {
        return dutyInfrastructure.getDutyByUserId(userId);
    }

    public Mono<Duty> saveDuty(Duty duty) {
        return dutyInfrastructure.saveDuty(duty);
    }

    public Mono<Duty> addRegularDutyByUserId(final String userId, final Pattern pattern) {
        return dutyInfrastructure.getDutyByUserId(userId)
                .switchIfEmpty(Mono.just(Duties.newDuty(userId)))
                .doOnNext(duty -> duty.addPattern(pattern))
                .flatMap(dutyInfrastructure::saveDuty);
    }

    public Mono<Duty> addRegularDutiesByUserId(final String userId, final Pattern... patterns) {
        return dutyInfrastructure.getDutyByUserId(userId)
                .switchIfEmpty(Mono.just(Duties.newDuty(userId)))
                .doOnNext(duty -> Arrays.stream(patterns).forEach(duty::addPattern))
                .flatMap(dutyInfrastructure::saveDuty);
    }

    public Mono<Duty> removeRegularDuty(final String userId, final Pattern pattern) {
        return dutyInfrastructure.getDutyByUserId(userId)
                .switchIfEmpty(Mono.just(Duties.newDuty(userId)))
                .doOnNext(duty -> duty.removePattern(pattern))
                .flatMap(dutyInfrastructure::saveDuty);
    }

    public Mono<Duty> addSpecialDutyByUserId(final String userId, final SpecialItem specialItem) {
        return dutyInfrastructure.getDutyByUserId(userId)
                .switchIfEmpty(Mono.just(Duties.newDuty(userId)))
                .doOnNext(duty -> duty.addSpecial(specialItem))
                .flatMap(dutyInfrastructure::saveDuty);
    }

    public Mono<Duty> addSpecialDutiesByUserId(final String userId, final SpecialItem... specialItems) {
        return dutyInfrastructure.getDutyByUserId(userId)
                .switchIfEmpty(Mono.just(Duties.newDuty(userId)))
                .doOnNext(duty -> Arrays.stream(specialItems).forEach(duty::addSpecial))
                .flatMap(dutyInfrastructure::saveDuty);
    }

    public Mono<AllDutyDetails> findAllDuties() {
        return dutyInfrastructure.findAllDuties()
                .flatMap(duty -> dutyManager.findUserInfoByUserId(duty.getUserId())
                        .map(userInfo -> new DutyDetail(userInfo.getUserId(), userInfo.getName(),
                                userInfo.getRole(), userInfo.getSex(), userInfo.getPhone(), userInfo.getEmail(),
                                duty.getPatterns(), duty.getSpecials()))
                )
                .collectList()
                .map(dutyDetails -> new AllDutyDetails(dutyDetails, dutyDetails.size()));
    }

    public Mono<Work> findWorkByUserId(String userId) {
        return dutyInfrastructure.getWorkByUserId(userId);
    }

    public Mono<Work> saveWork(Work work) {
        return dutyInfrastructure.saveWork(work);
    }

    public Mono<Work> addWorkByUserId(final String userId, final WorkItem workItem) {
        return dutyInfrastructure.getWorkByUserId(userId)
                .switchIfEmpty(Mono.just(Works.newWork(userId)))
                .doOnNext(work -> work.addWork(workItem))
                .flatMap(dutyInfrastructure::saveWork);
    }

    public Mono<Work> addWorksByUserId(final String userId, final WorkItem... workItems) {
        return dutyInfrastructure.getWorkByUserId(userId)
                .switchIfEmpty(Mono.just(Works.newWork(userId)))
                .doOnNext(work -> Arrays.stream(workItems).forEach(work::addWork))
                .flatMap(dutyInfrastructure::saveWork);
    }

    public Mono<Work> deleteWorkByUserId(String userId, WorkItem workItem) {
        return dutyInfrastructure.getWorkByUserId(userId)
                .switchIfEmpty(Mono.just(Works.newWork(userId)))
                .doOnNext(work -> work.removeWork(workItem))
                .flatMap(dutyInfrastructure::saveWork);
    }

    public Flux<LocalDate> addWorkItemOfTimesForUser(final String userId, final int weekday, final int from,
                                                           final String address, final int times) {
        return dutyInfrastructure.getWorkByUserId(userId)
                .doOnNext(work -> log.info("Got Work -> {}", work))
                .map(work -> work.arrangeWorks(weekday, from, address, times))
                .flatMap(dutyInfrastructure::saveWork)
                .map(work -> work.getWorks().subList(work.getWorks().size() - times, work.getWorks().size()))
                .map(list -> list.stream().map(WorkItem::getDay))
                .flatMapMany(Flux::fromStream);
    }
}
