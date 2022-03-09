package cn.tzq0301.consult.handler;

import cn.tzq0301.consult.entity.Record;
import cn.tzq0301.consult.entity.consultor.FinishConsult;
import cn.tzq0301.consult.service.ConsultService;
import cn.tzq0301.entity.Records;
import cn.tzq0301.entity.RecordsWithTotal;
import cn.tzq0301.result.Result;
import cn.tzq0301.util.JWTUtils;
import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * @author tzq0301
 * @version 1.0
 */
@Component
@AllArgsConstructor
public class ConsultHandler {
    private final ConsultService consultService;

    public Mono<ServerResponse> generateConsult(ServerRequest request) {
        int weekday = Integer.parseInt(request.pathVariable("weekday"));
        int from = Integer.parseInt(request.pathVariable("from"));
        String address = request.pathVariable("address");
        String consultorId = request.pathVariable("consultor_id");

        return consultService.generateConsult(new ObjectId(request.pathVariable("global_id")),
                        weekday, from, address, consultorId)
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> listStudentConsultByStudentId(ServerRequest request) {
        String studentId = request.pathVariable("user_id");
        String studentIdFromJWT = JWTUtils.extractUserId(getJWT(request));

        if (!Objects.equals(studentId, studentIdFromJWT)) {
            return Mono.just(Result.error(1, "用户 ID 不匹配"))
                    .flatMap(ServerResponse.ok()::bodyValue);
        }

        int offset = getOffset(request);
        int limit = getLimit(request);

        return consultService.listStudentConsultByStudentId(studentId)
                .map(studentConsults -> new RecordsWithTotal<>(studentConsults, offset, limit))
                .map(Result::success)
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> findStudentConsultDetailByGlobalId(ServerRequest request) {
        String studentId = request.pathVariable("user_id");
        String studentIdFromJWT = JWTUtils.extractUserId(getJWT(request));

        if (!Objects.equals(studentId, studentIdFromJWT)) {
            return Mono.just(Result.error(1, "用户 ID 不匹配"))
                    .flatMap(ServerResponse.ok()::bodyValue);
        }

        String globalId = request.pathVariable("global_id");
        return consultService.findStudentConsultDetailByGlobalId(globalId)
                .map(Result::success)
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> listAllConsultRecordsForAssistant(ServerRequest request) {
        return consultService.listAllConsultRecordsForAssistant()
                .map(Records::new)
                .map(Result::success)
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> listAllConsultRecordsOfConsultorByConsultorId(ServerRequest request) {
        String consultorId = request.pathVariable("consultor_id");

        return consultService.listAllConsultRecordsOfConsultorByConsultorId(consultorId)
                .map(Records::new)
                .map(Result::success)
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> findConsultRecordForConsultorByGlobalId(ServerRequest request) {
        String consultorId = request.pathVariable("consultor_id");
        String consultorIdFromJWT = JWTUtils.extractUserId(getJWT(request));

        if (!Objects.equals(consultorId, consultorIdFromJWT)) {
            return Mono.just(Result.error(1, "用户 ID 不匹配"))
                    .flatMap(ServerResponse.ok()::bodyValue);
        }

        String globalId = request.pathVariable("global_id");
        return consultService.findConsultRecordForConsultorByGlobalId(globalId)
                .map(Result::success)
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> commitRecordByGlobalId(ServerRequest request) {
        final String globalId = request.pathVariable("global_id");

        Mono<Record> recordMono = request.bodyToMono(Record.class);

        return consultService.commitRecordByGlobalId(globalId, recordMono)
                .flatMap(it -> ServerResponse.ok().bodyValue(Result.success()));
    }

    public Mono<ServerResponse> finishConsultByGlobalId(ServerRequest request) {
        return consultService.finishConsultByGlobalId(
                request.pathVariable("global_id"), request.bodyToMono(FinishConsult.class))
                .map(it -> Result.success())
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> listAllStaticsInfos(ServerRequest request) {
        return consultService.listAllStaticsInfos()
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> findPdfInfoByGlobalId(ServerRequest request) {
        String globalId = request.pathVariable("global_id");
        return consultService.findPdfInfoByGlobalId(globalId)
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

    /**
     * 获取请求参数中的 offset
     *
     * @return offset（默认值为 0）
     */
    private int getOffset(ServerRequest request) {
        String offset = request.exchange().getRequest().getQueryParams().getFirst("offset");

        if (Strings.isNullOrEmpty(offset)) {
            return 0;
        }

        return Integer.parseInt(offset);
    }

    /**
     * 获取请求参数中的 limit
     *
     * @return limit（默认值为 {@code Integer.MAX_VALUE}）
     */
    private int getLimit(ServerRequest request) {
        String limit = request.exchange().getRequest().getQueryParams().getFirst("limit");

        if (Strings.isNullOrEmpty(limit)) {
            return Integer.MAX_VALUE;
        }

        return Integer.parseInt(limit);
    }

    /**
     * 获取请求参数中的 str
     *
     * @return str
     */
    private String getStr(ServerRequest request) {
        return request.exchange().getRequest().getQueryParams().getFirst("str");
    }
}
