package cn.tzq0301.statics.handler;

import cn.tzq0301.entity.RecordsWithTotal;
import cn.tzq0301.result.Result;
import cn.tzq0301.statics.entity.StaticsInfo;
import cn.tzq0301.statics.service.StaticsService;
import cn.tzq0301.util.JWTUtils;
import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * @author tzq0301
 * @version 1.0
 */
@Component
@AllArgsConstructor
@Log4j2
public class StaticsHandler {
    private final StaticsService staticsService;

    public Mono<ServerResponse> listInfos(ServerRequest request) {
        String name = getAttribute(request, "name");
        String tempProblemId = getAttribute(request, "problemId");
        int problemId = Strings.isNullOrEmpty(tempProblemId) ? -1 : Integer.parseInt(tempProblemId);

        int offset = getOffset(request);
        int limit = getLimit(request);

        if ((problemId < 0 || problemId > 3) && Strings.isNullOrEmpty(name)) {
            return staticsService.listAllStaticsInfos()
                    .map(list -> new RecordsWithTotal<>(list, offset, limit))
                    .map(Result::success)
                    .flatMap(ServerResponse.ok()::bodyValue);
        }

        if ((problemId >= 0 && problemId <= 3) && Strings.isNullOrEmpty(name)) {
            return staticsService.listAllStaticsInfos()
                    .map(list -> new RecordsWithTotal<>(list,
                            info -> Objects.equals(info.getProblemId(), problemId), offset, limit))
                    .map(Result::success)
                    .flatMap(ServerResponse.ok()::bodyValue);
        }

        if ((problemId < 0 || problemId > 3) && !Strings.isNullOrEmpty(name)) {
            return staticsService.listAllStaticsInfos()
                    .map(list -> new RecordsWithTotal<>(list, info ->
                            info.getStudentName().contains(name)
                                    || info.getVisitorName().contains(name)
                                    || info.getConsultorName().contains(name),
                            offset, limit))
                    .map(Result::success)
                    .flatMap(ServerResponse.ok()::bodyValue);
        }

        return staticsService.listAllStaticsInfos()
                .map(list -> new RecordsWithTotal<>(
                        list,
                        info -> Objects.equals(info.getProblemId(), problemId) && (
                                info.getStudentName().contains(name)
                                        || info.getVisitorName().contains(name)
                                        || info.getConsultorName().contains(name))
                        , offset, limit))
                .map(Result::success)
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> exportConsultReports(ServerRequest request) {
        return request.bodyToMono(new ParameterizedTypeReference<List<String>>() {
                })
                .doOnNext(it -> log.info("Global IDs -> {}", it))
                .flatMapMany(Flux::fromIterable)
                .flatMap(staticsService::exportConsultReport)
                .collectList()
                .map(Result::success)
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> exportConsultReportByGlobalId(ServerRequest request) {
        return Mono.just(request.pathVariable("global_id"))
                .flatMap(staticsService::exportConsultReport)
                .map(Result::success)
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> exportConsultorInfos(ServerRequest request) {
        return staticsService.exportConsultorInfo()
                .map(Result::success)
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
     * 获取请求参数中的属性
     *
     * @return 属性
     */
    private String getAttribute(ServerRequest request, final String attribute) {
        return request.exchange().getRequest().getQueryParams().getFirst(attribute);
    }
}
