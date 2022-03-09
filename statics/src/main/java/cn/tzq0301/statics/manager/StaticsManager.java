package cn.tzq0301.statics.manager;

import cn.tzq0301.statics.entity.PdfInfo;
import cn.tzq0301.statics.entity.StaticsInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author tzq0301
 * @version 1.0
 */
@Service
@RefreshScope
public class StaticsManager {
    private final WebClient.Builder builder;

    private final WebClient.Builder builderWithOutLoadBalanced;

    @Value("${go.host}")
    private String host;

    @Value("${go.port}")
    private String port;

    public StaticsManager(WebClient.Builder builder, WebClient.Builder builderWithOutLoadBalanced) {
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
}
