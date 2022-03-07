package cn.tzq0301.visit.record.handler;

import cn.tzq0301.entity.Records;
import cn.tzq0301.result.Result;
import cn.tzq0301.util.DateUtils;
import cn.tzq0301.util.JWTUtils;
import cn.tzq0301.visit.record.entity.VisitRecord;
import cn.tzq0301.visit.record.entity.vo.ResponsibleVisitRecord;
import cn.tzq0301.visit.record.entity.vo.ResponsibleVisitRecordDetail;
import cn.tzq0301.visit.record.entity.vo.VisitRecordSubmitRequest;
import cn.tzq0301.visit.record.service.VisitRecordService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.stream.Collectors;

import static cn.tzq0301.util.Num.ONE;
import static cn.tzq0301.util.Num.ZERO;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * @author tzq0301
 * @version 1.0
 */
@Component
@AllArgsConstructor
public class VisitHandler {
    private final VisitRecordService visitRecordService;

    public Mono<ServerResponse> listVisitRecordsByVisitorId(ServerRequest request) {
        String visitorId = request.pathVariable("visitor_id");
        String visitorIdFromJWT = getUserId(request);

        if (!Objects.equals(visitorId, visitorIdFromJWT)) {
            return ServerResponse.ok().bodyValue(Result.error(1, "用户 ID 不匹配"));
        }

        return visitRecordService.findVisitRecordByVisitorId(visitorId)
                .map(list -> list.stream()
                        .map(visitRecord -> new ResponsibleVisitRecord(visitRecord.getId().toString(),
                                visitRecord.getStudentId(), visitRecord.getStudentName(), visitRecord.getStudentSex(),
                                visitRecord.getStudentPhone(), DateUtils.localDateToString(visitRecord.getDay()),
                                visitRecord.getFrom(), visitRecord.getAddress(), visitRecord.getStatus()))
                        .collect(Collectors.toList()))
                .map(Records::new)
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> listSpecificVisitRecord(ServerRequest request) {
        String visitorId = request.pathVariable("user_id");
        String visitorIdFromJWT = getUserId(request);

        if (!Objects.equals(visitorId, visitorIdFromJWT)) {
            return ServerResponse.ok().bodyValue(Result.error(1, "用户 ID 不匹配"));
        }

        String globalId = request.pathVariable("global_id");

        return visitRecordService.findVisitRecordById(globalId)
                .map(visitRecord -> new ResponsibleVisitRecordDetail(visitRecord.getId().toString(),
                        visitRecord.getStudentId(), visitRecord.getStudentName(), visitRecord.getStudentSex(),
                        visitRecord.getStudentPhone(), DateUtils.localDateToString(visitRecord.getDay()),
                        visitRecord.getFrom(), visitRecord.getAddress(), visitRecord.getStatus(),
                        visitRecord.getDangerLevel(), visitRecord.getProblemId(), visitRecord.getProblemDetail()))
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> submit(ServerRequest request) {
        String globalId = request.pathVariable("global_id");

        return Mono.zip(
                        request.bodyToMono(VisitRecordSubmitRequest.class),
                        visitRecordService.findVisitRecordById(globalId)
                )
                .map(tuple -> {
                    VisitRecordSubmitRequest submitRequest = tuple.getT1();
                    VisitRecord visitRecord = tuple.getT2();
                    visitRecord.setDangerLevel(submitRequest.getDangerLevel());
                    visitRecord.setResult(submitRequest.getResult());
                    visitRecord.setProblemId(submitRequest.getProblemId());
                    visitRecord.setProblemDetail(submitRequest.getProblemDetail());
                    visitRecord.setStatus(ONE);
                    visitRecord.setConsultApplyStatus(ZERO);
                    return visitRecord;
                })
                .flatMap(visitRecordService::saveVisitRecord)
                .map(it -> Result.success())
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    private String getJWT(ServerRequest request) {
        return Objects.requireNonNull(request.headers().firstHeader(AUTHORIZATION))
                .substring(JWTUtils.AUTHORIZATION_HEADER_PREFIX.length());
    }

    private String getUserId(String jwt) {
        return JWTUtils.extractUserId(jwt);
    }

    private String getUserId(ServerRequest request) {
        return JWTUtils.extractUserId(Objects.requireNonNull(
                        request.headers().firstHeader(AUTHORIZATION))
                .substring(JWTUtils.AUTHORIZATION_HEADER_PREFIX.length()));
    }
}
