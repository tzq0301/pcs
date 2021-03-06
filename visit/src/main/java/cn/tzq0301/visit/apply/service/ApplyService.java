package cn.tzq0301.visit.apply.service;

import cn.tzq0301.util.DateUtils;
import cn.tzq0301.visit.apply.entity.Applies;
import cn.tzq0301.visit.apply.entity.Apply;
import cn.tzq0301.visit.apply.entity.UserInfo;
import cn.tzq0301.visit.apply.entity.all.FirstRecord;
import cn.tzq0301.visit.apply.entity.applyrequest.ApplyRequest;
import cn.tzq0301.visit.apply.entity.applyrequest.ApplyRequestException;
import cn.tzq0301.visit.apply.entity.passapply.PassApplyRequest;
import cn.tzq0301.visit.apply.entity.unfinished.UnfinishedApply;
import cn.tzq0301.visit.apply.infrastructure.ApplyInfrastructure;
import cn.tzq0301.visit.apply.manager.ApplyManager;
import cn.tzq0301.visit.record.entity.VisitRecord;
import cn.tzq0301.visit.record.entity.VisitRecords;
import cn.tzq0301.visit.record.infrastructure.VisitRecordInfrastructure;
import cn.tzq0301.visit.record.manager.VisitRecordManager;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuple3;

import static cn.tzq0301.util.Num.ONE;
import static cn.tzq0301.util.Num.ZERO;
import static cn.tzq0301.visit.apply.entity.ApplyStatusEnum.PENDING_REVIEW;

/**
 * @author tzq0301
 * @version 1.0
 */
@Service
@AllArgsConstructor
@Log4j2
public class ApplyService {
    private final ApplyManager applyManager;

    private final VisitRecordManager visitRecordManager;

    private final ApplyInfrastructure applyInfrastructure;

    private final VisitRecordInfrastructure visitRecordInfrastructure;

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
                        .flatMap(apply -> applyInfrastructure.saveApply(apply)
                                .flatMap(it -> applyManager.setStudentStatus(it.getUserId(), ONE)
                                        .flatMap(status -> ONE.equals(status)
                                                ? Mono.just(apply)
                                                : Mono.error(ApplyRequestException::new)))))
                .switchIfEmpty(Mono.error(ApplyRequestException::new));
    }

    public Mono<Apply> getApplyByApplyId(String applyId) {
        return applyInfrastructure.findApplyByApplyId(applyId);
    }

    public Flux<Apply> getAppliesByUserId(final String userId) {
        return applyInfrastructure.listAppliesByUserId(userId);
    }

    public Flux<UnfinishedApply> getAllUnfinishedApplies() {
        return applyInfrastructure.listAllAppliesByStatus(PENDING_REVIEW.getCode())
                .flatMap(apply -> applyManager.findUserInfoByUserId(apply.getVisitorId())
                        .map(userInfo -> new UnfinishedApply(apply.getId().toString(), apply.getUserId(),
                                apply.getName(), apply.getSex(), apply.getPhone(), apply.getEmail(),
                                apply.getProblemId(), apply.getProblemDetail(), apply.getOrder(),
                                DateUtils.localDateToString(apply.getDay()),
                                apply.getFrom(), apply.getAddress(), apply.getVisitorId(), userInfo.getName())));
    }

    public Flux<FirstRecord> getAllApplies() {
        return applyInfrastructure.listAllApplies()
                .flatMap(apply -> applyManager.findUserInfoByUserId(apply.getVisitorId())
                        .map(userInfo -> new FirstRecord(apply.getId().toString(), apply.getUserId(),
                                apply.getName(), apply.getSex(), apply.getPhone(), apply.getEmail(),
                                apply.getProblemId(), apply.getProblemDetail(), apply.getOrder(),
                                DateUtils.localDateToString(apply.getDay()),
                                apply.getFrom(), apply.getAddress(), apply.getVisitorId(), userInfo.getName(),
                                userInfo.getPhone(), userInfo.getEmail(), apply.getStatus())));
    }

    public Mono<Tuple2<Apply, Integer>> revokeUnPassedApply(final Apply apply) {
        apply.revoke();

        return Mono.zip(
                applyInfrastructure.saveApply(apply),
                applyManager.setStudentStatus(apply.getUserId(), 0)
        );
    }

    // ???????????????????????????????????????????????????????????????????????????
    public Mono<Tuple3<Apply, String, Integer>> revokeApply(final Apply apply) {
        apply.revoke();

        return Mono.zip(
                // 1. ????????????????????????
                applyInfrastructure.saveApply(apply),
                // 2. ???????????????????????????
                applyManager.deleteVisitorWorkById(apply.getId()),
                applyManager.setStudentStatus(apply.getUserId(), 0)
        );
    }

    /**
     * ??????????????????
     *
     * <ol>
     *     <li>?????? globalId ??? {@link Apply} ??????</li>
     *     <li>?????? visitorId ??? {@link UserInfo} ??????</li>
     *     <li>?????? visitorId???day???from ?????? address</li>
     *     <li>????????????????????????</li>
     *     <li>?????????????????? {@link VisitRecord}</li>
     *     <li>??????????????????????????????</li>
     * </ol>
     *
     * @param passApplyRequest ????????????????????????
     * @return ????????????
     */
    public Mono<?> passApply(PassApplyRequest passApplyRequest) {
        return Mono.zip(
                        // 1.1. ?????? globalId ??? Apply ??????
                        applyInfrastructure.findApplyByApplyId(passApplyRequest.getGlobalId()),
                        // 1.2. ?????? visitorId ??? UserInfo
                        applyManager.findUserInfoByUserId(passApplyRequest.getVisitorId()),
                        // 1.3. ?????? visitorId???day???from ?????? address
                        applyManager.findAddressByUserIdAndDayAndFrom(passApplyRequest.getVisitorId(),
                                passApplyRequest.getDay(), passApplyRequest.getFrom()))
                .flatMap(tuple3 -> {
                    // 2.1. ????????????????????????
                    tuple3.getT1().pass();
                    // 2.2. ??????????????????
                    return applyInfrastructure.saveApply(tuple3.getT1())
                            .map(it -> VisitRecords.newVisitRecord(tuple3.getT1(), tuple3.getT2(), passApplyRequest.getProblemId(),
                                    passApplyRequest.getProblemDetail(), passApplyRequest.getDay(), passApplyRequest.getFrom(), tuple3.getT3()));
                })
                .doOnNext(visitRecord -> log.info("Save visit record: {}", visitRecord.toString()))
                .flatMap(visitRecordInfrastructure::saveVisitRecord)
                // 2.3. ??????????????????????????????
                .flatMap(visitRecord -> visitRecordManager.addWorkByUserId(visitRecord.getVisitorId(), visitRecord.getDay(), visitRecord.getFrom(), visitRecord.getAddress()));
    }

    public Mono<?> rejectApply(final String applyId) {
        return applyInfrastructure.findApplyByApplyId(applyId)
                .flatMap(apply -> {
                    apply.reject();
                    return Mono.zip(applyInfrastructure.saveApply(apply),
                            applyManager.setStudentStatus(apply.getUserId(), ZERO));
//                    return applyInfrastructure.saveApply(apply);
                })
                .map(Tuple2::getT1)
                .doOnNext(apply -> log.info("Save Apply: {}", apply.toString()));
    }

    public Mono<Void> deleteApplyById(final String globalId) {
        return applyInfrastructure.deleteApplyById(globalId);
    }
}
