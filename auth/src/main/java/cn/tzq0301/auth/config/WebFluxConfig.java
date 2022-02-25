package cn.tzq0301.auth.config;

import cn.tzq0301.auth.handler.GlobalResponseBodyResultHandler;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.accept.RequestedContentTypeResolver;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.result.method.annotation.ResponseBodyResultHandler;

/**
 * @author tzq0301
 * @version 1.0
 */
@SpringBootConfiguration
public class WebFluxConfig implements WebFluxConfigurer {
    @Bean
    public ResponseBodyResultHandler responseWrapper(ServerCodecConfigurer serverCodecConfigurer,
                                                     RequestedContentTypeResolver requestedContentTypeResolver) {
        return new GlobalResponseBodyResultHandler(serverCodecConfigurer.getWriters(), requestedContentTypeResolver);
    }
}
