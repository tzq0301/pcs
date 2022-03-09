package cn.tzq0301.statics.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author tzq0301
 * @version 1.0
 */
@SpringBootConfiguration
public class WebClientConfig {
    @Bean
    public WebClient.Builder builder() {
        return WebClient.builder();
    }
}
