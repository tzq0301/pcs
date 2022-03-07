package cn.tzq0301.visit.record.service;

import cn.tzq0301.visit.record.entity.VisitRecord;
import cn.tzq0301.visit.record.infrastructure.VisitRecordInfrastructure;
import cn.tzq0301.visit.record.manager.VisitRecordManager;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * @author tzq0301
 * @version 1.0
 */
@Service
@AllArgsConstructor
@Log4j2
public class VisitRecordService {
    private final VisitRecordInfrastructure visitRecordInfrastructure;

    private final VisitRecordManager visitRecordManager;

    public Mono<VisitRecord> findVisitRecordById(String id) {
        return visitRecordInfrastructure.findVisitRecordById(id);
    }

    public Mono<VisitRecord> findVisitRecordById(ObjectId id) {
        log.info("try to find VisitRecord of ID {}", id);
        return visitRecordInfrastructure.findVisitRecordById(id);
    }

    public Mono<VisitRecord> saveVisitRecord(final VisitRecord visitRecord) {
        return visitRecordInfrastructure.saveVisitRecord(visitRecord);
    }

    /**
     * 删除初访记录（同时删除初访员工作安排）
     *
     * @param visitRecordId 初访记录 ID
     * @return 返回结果
     */
    public Mono<String> deleteVisitorWorkById(final ObjectId visitRecordId) {
        return visitRecordInfrastructure.findVisitRecordById(visitRecordId)
                .doOnNext(visitRecord -> log.info("Got VisitRecord: {}", visitRecord))
                .flatMap(visitRecord -> visitRecordManager.deleteWorkByUserId(visitRecord.getVisitorId(),
                        visitRecord.getDay(), visitRecord.getFrom(), visitRecord.getAddress()));
    }
}
