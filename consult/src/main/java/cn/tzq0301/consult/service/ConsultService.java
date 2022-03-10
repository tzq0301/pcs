package cn.tzq0301.consult.service;

import cn.tzq0301.consult.entity.Consult;
import cn.tzq0301.consult.entity.Consults;
import cn.tzq0301.consult.entity.Record;
import cn.tzq0301.consult.entity.UserInfo;
import cn.tzq0301.consult.entity.assistant.ConsultRecordForAssistant;
import cn.tzq0301.consult.entity.consultor.ConsultRecordForConsultor;
import cn.tzq0301.consult.entity.consultor.ConsultRecordOfConsultor;
import cn.tzq0301.consult.entity.consultor.FinishConsult;
import cn.tzq0301.consult.entity.statics.ConsultRecord;
import cn.tzq0301.consult.entity.statics.ConsultorStaticsInfo;
import cn.tzq0301.consult.entity.statics.PdfInfo;
import cn.tzq0301.consult.entity.statics.StaticsInfo;
import cn.tzq0301.consult.entity.student.StudentConsult;
import cn.tzq0301.consult.entity.student.StudentConsultDetail;
import cn.tzq0301.consult.entity.visit.VisitRecord;
import cn.tzq0301.consult.infrastructure.ConsultInfrastructure;
import cn.tzq0301.consult.manager.ConsultManager;
import cn.tzq0301.problem.ProblemEnum;
import cn.tzq0301.util.DateUtils;
import cn.tzq0301.util.SexUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

import static cn.tzq0301.util.Num.*;

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

    public Mono<ConsultRecordForConsultor> findConsultRecordForConsultorByGlobalId(final String globalId) {
        return consultInfrastructure.findConsultById(new ObjectId(globalId))
                .map(consult -> new ConsultRecordForConsultor(globalId, consult.getStudentId(), consult.getStudentName(),
                        SexUtils.sexOfString(consult.getStudentSex()), consult.getStudentPhone(), consult.getScaleResult(),
                        consult.getVisitorName(), consult.getProblemId(), consult.getProblemDetail(), consult.getDangerLevel(),
                        consult.getTimes(), consult.getConsultStatus(), consult.getRecords(), consult.getPattern(),
                        consult.getSelfComment(), consult.getDetail()));
    }

    public Mono<Consult> commitRecordByGlobalId(final String globalId, final Mono<Record> recordMono) {
        return Mono.zip(consultInfrastructure.findConsultById(globalId), recordMono)
                .flatMap(tuple -> {
                    Consult consult = tuple.getT1();
                    Record record = tuple.getT2();
                    List<Record> noUse = consult.addRecord(record);

                    // 如果是提前结案，删除多余的咨询师工作安排
                    if (consult.isFinishedAdvanced()) {
                        return Flux.fromIterable(noUse)
                                .flatMap(it -> consultManager.deleteWorkItemByUserId(consult.getConsultorId(),
                                        it.getDay(), it.getFrom(), it.getAddress()))
                                .collectList()
                                .map(it -> consult);
                    }
                    // 如果是仍需追加，则增加咨询师工作安排
                    else if (FOUR.equals(record.getStatus())) {
                        return consultManager.addWorkItemForUser(consult.getConsultorId(), consult.getPattern())
                                .doOnNext(day -> consult.arrangeTheLastRecord(new Record("", FIVE,
                                        DateUtils.localDateToString(day), consult.getFrom(), consult.getAddress())))
                                .map(it -> consult);
                    }

                    return Mono.just(consult);
                })
                .flatMap(consultInfrastructure::saveConsult);
    }

    public Mono<Consult> finishConsultByGlobalId(final String globalId, final Mono<FinishConsult> finishConsultMono) {
        return Mono.zip(consultInfrastructure.findConsultById(globalId), finishConsultMono)
                .flatMap(tuple -> {
                    Consult consult = tuple.getT1();
                    FinishConsult finishConsult = tuple.getT2();

                    consult.setConsultStatus(TWO);
                    consult.setSelfComment(finishConsult.getSelfComment());
                    consult.setDetail(finishConsult.getDetail());

                    return Mono.zip(Mono.just(consult), consultManager.setStudentStatus(consult.getStudentId(), 0));
                })
                .flatMap(tuple -> consultInfrastructure.saveConsult(tuple.getT1()));
    }

    public Mono<List<StaticsInfo>> listAllStaticsInfos() {
        return consultInfrastructure.findAllConsults()
                .map(consult -> new StaticsInfo(consult.getId().toString(),
                        consult.getStudentName(), consult.getStudentPhone(),
                        consult.getVisitorName(), consult.getVisitorPhone(),
                        consult.getConsultorName(), consult.getConsultorPhone(),
                        consult.getProblemId(), consult.getProblemDetail()))
                .collectList();
    }

    public Mono<PdfInfo> findPdfInfoByGlobalId(final String globalId) {
        log.info("Global ID -> {}", globalId);
        return consultInfrastructure.findConsultById(globalId)
                .map(consult -> new PdfInfo(consult.getStudentName(), consult.getStudentSex(),
                        consult.getStudentPhone(), consult.getStudentEmail(), consult.getStudentBirthday(),
                        consult.getConsultorName(), consult.getConsultorSex(), consult.getConsultorPhone(),
                        consult.getConsultorEmail(), consult.getSelfComment(), consult.getDetail(),
                        consult.getRecords().stream().map(record -> new ConsultRecord(
                                DateUtils.formatToChineseDateString(record.getDay()),
                                record.getAddress(), record.getDetail()))
                                .collect(Collectors.toList()), consult.getCreatedTime()))
                .doOnNext(pdfInfo -> log.info("Got information for PDF -> {}", pdfInfo));
    }

    public Mono<ConsultorStaticsInfo> findStaticsInfoByConsultorId(final String consultorId) {
        return consultInfrastructure.listConsultsByConsultorId(consultorId).collectList()
                .map(list -> new ConsultorStaticsInfo(
                        (long) list.size(),
                        list.stream()
                                .map(Consult::getTimes)
                                .map(Long::valueOf)
                                .reduce(0L, Long::sum)));
    }
}
