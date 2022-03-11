package cn.tzq0301.statics.manager;

import cn.tzq0301.statics.entity.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author tzq0301
 * @version 1.0
 */
@Service
@RefreshScope
@Log4j2
public class StaticsManager {
    private final WebClient.Builder builder;

    private final WebClient.Builder builderWithOutLoadBalanced;

    @Value("${go.host}")
    private String host;

    @Value("${go.port}")
    private String port;

    public StaticsManager(
            WebClient.Builder builder,
            WebClient.Builder builderWithOutLoadBalanced) {
        this.builder = builder;
        this.builderWithOutLoadBalanced = builderWithOutLoadBalanced;
    }

    public Mono<List<StaticsInfo>> listAllStaticsInfos() {
        return builder.build().get()
                .uri("lb://pcs-consult/statics_infos")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<StaticsInfo>>() {
                });
    }

    public Mono<PdfInfo> findPdfInfoByGlobalId(final String globalId) {
        log.info("Global ID -> {}", globalId);
        return builder.build().get()
                .uri("lb://pcs-consult/pdf_info/global_id/{global_id}", globalId)
                .retrieve()
                .bodyToMono(PdfInfo.class);
    }

    public Mono<String> exportPdf(final PdfInfo pdfInfo) {
        return builderWithOutLoadBalanced.build().post()
                .uri("http://" + host + ":" + port + "/export/pdf")
                .bodyValue(pdfInfo)
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> exportPdfsPackedByZip(final Flux<PdfInfo> pdfInfo) {
        return builderWithOutLoadBalanced.build().post()
                .uri("http://" + host + ":" + port + "/export/zip")
                .body(pdfInfo, PdfInfo.class)
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> exportPdfsPackedByZip(final Mono<List<PdfInfo>> pdfInfo) {
        return builderWithOutLoadBalanced.build().post()
                .uri("http://" + host + ":" + port + "/export/zip")
                .body(pdfInfo, PdfInfo.class)
                .retrieve()
                .bodyToMono(String.class);
    }

    public Flux<UserInfo> listConsultorInfos() {
        return builder.build().get()
                .uri("lb://pcs-auth/user_infos/role/{role}", "CONSULTANT")
                .retrieve()
                .bodyToFlux(UserInfo.class)
                .doOnNext(userInfo -> log.info("Got UserInfo -> {}", userInfo));
    }

    public Mono<ConsultorStaticsInfo> findConsultorStaticsInfo(final String consultorId) {
        return builder.build().get()
                .uri("lb://pcs-consult/consultor_id/{consultor_id}/statics_info", consultorId)
                .retrieve()
                .bodyToMono(ConsultorStaticsInfo.class)
                .doOnNext(consultorStaticsInfo -> log.info("Got ConsultorStaticsInfo -> {}", consultorStaticsInfo));
    }

    public Mono<String> exportCsv(final Flux<CsvInfo> csvInfoFlux) {
        return builderWithOutLoadBalanced.build().post()
                .uri("http://" + host + ":" + port + "/export/csv")
                .body(csvInfoFlux, CsvInfo.class)
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(url -> log.info("Got URL -> {}", url));
    }
}
