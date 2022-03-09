package cn.tzq0301.statics.manager;

import cn.tzq0301.entity.RecordsWithTotal;
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

    @Value("${go.host}")
    private String host;

    @Value("${go.port}")
    private String port;

    public StaticsManager(WebClient.Builder builder) {
        this.builder = builder;
    }

    public Mono<List<StaticsInfo>> listAllStaticsInfos() {
        return builder.build().get()
                .uri("lb://pcs-consult/statics_infos")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<StaticsInfo>>() {
                });
    }
}
