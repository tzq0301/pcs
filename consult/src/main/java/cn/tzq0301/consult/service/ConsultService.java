package cn.tzq0301.consult.service;

import cn.tzq0301.consult.entity.Consult;
import cn.tzq0301.consult.entity.Consults;
import cn.tzq0301.consult.entity.Record;
import cn.tzq0301.consult.entity.UserInfo;
import cn.tzq0301.consult.entity.assistant.ConsultRecordForAssistant;
import cn.tzq0301.consult.entity.consultor.ConsultRecordOfConsultor;
import cn.tzq0301.consult.entity.student.StudentConsult;
import cn.tzq0301.consult.entity.student.StudentConsultDetail;
import cn.tzq0301.consult.entity.visit.VisitRecord;
import cn.tzq0301.consult.entity.work.WorkArrange;
import cn.tzq0301.consult.infrastructure.ConsultInfrastructure;
import cn.tzq0301.consult.manager.ConsultManager;
import cn.tzq0301.problem.ProblemEnum;
import cn.tzq0301.util.DateUtils;
import cn.tzq0301.util.SexUtils;
import jdk.jfr.Frequency;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static cn.tzq0301.util.Num.FIVE;
import static cn.tzq0301.util.Num.ZERO;

/**
 * @author tzq0301
 * @version 1.0
 */
@Service
@AllArgsConstructor
@Log4j2
public class ConsultService {
    private final ConsultManager consultManager;

    private final ConsultInfrastructure consultInfrastructure;

    // 返回第一次的时间
    public Mono<String> generateConsult(final ObjectId id, int weekday, int from, String address, String consultorId) {
        log.info("Global ID -> {}", id.toHexString());
        return Mono.zip(
                        consultManager.findVisitRecordById(id),
                        consultManager.findUserInfoByUserId(consultorId))
                .flatMap(tuple -> {
                    VisitRecord visitRecord = tuple.getT1();
                    log.info("Got VisitRecord -> {}", visitRecord);
                    UserInfo consultor = tuple.getT2();
                    log.info("Got UserInfo -> {}", consultor);
                    return consultManager.addWorkItemsForUser(consultor.getUserId(), weekday, from, address, 8).collectList()
                            .map(list -> Consults.newConsult(id.toString(), visitRecord, consultor, weekday, from, address, list.stream()
                                    .map(day -> new Record("", FIVE, DateUtils.localDateToString(day), from, address))
                                    .collect(Collectors.toList())))
                            .flatMap(consultInfrastructure::saveConsult)
                            .doOnNext(consult -> log.info("Save Consult -> {}", consult))
                            .flatMap(consult -> consultManager.passVisitRecord(id.toString()).map(it -> consult));
                })
                .map(consult -> consult.getRecords().get(0).getDay());
    }

    public Mono<List<StudentConsult>> listStudentConsultByStudentId(String studentId) {
        return consultInfrastructure.findConsultByStudentId(studentId)
                .map(consult -> new StudentConsult(consult.getId().toString(), consult.getConsultorName(),
                        consult.getConsultorPhone(), consult.getTimes(), consult.getConsultStatus()))
                .collectList();
    }

    public Mono<StudentConsultDetail> findStudentConsultDetailByGlobalId(final String globalId) {
        return consultInfrastructure.findConsultById(globalId)
                .map(consult -> new StudentConsultDetail(consult.getRecords(), consult.getDangerLevel(),
                        ProblemEnum.getName(consult.getProblemId()), consult.getProblemDetail(),
                        consult.getConsultorName(), SexUtils.sexOfString(consult.getConsultorSex()),
                        consult.getConsultorPhone(), consult.getConsultorEmail(), consult.getTimes()));
    }

    public Mono<List<ConsultRecordForAssistant>> listAllConsultRecordsForAssistant() {
        return consultInfrastructure.findAllConsults()
                .map(consult -> new ConsultRecordForAssistant(consult.getId().toString(), consult.getStudentId(),
                        consult.getStudentName(), consult.getConsultorId(), consult.getConsultorName(),
                        consult.getTimes(), consult.getConsultStatus(), consult.getPattern()))
                .collectList();
    }

    public Mono<List<ConsultRecordOfConsultor>> listAllConsultRecordsOfConsultorByConsultorId(final String consultorId) {
        return consultInfrastructure.findAllConsultsByConsultorId(consultorId)
                .map(consult -> new ConsultRecordOfConsultor(consult.getId().toString(), consult.getStudentId(),
                        consult.getStudentName(), consult.getStudentPhone(), consult.getTimes(),
                        consult.getConsultStatus(), consult.getPattern()))
                .collectList();
    }
}
