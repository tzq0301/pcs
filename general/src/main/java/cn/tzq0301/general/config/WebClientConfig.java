package cn.tzq0301.general.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author tzq0301
 * @version 1.0
 */
@SpringBootConfiguration
public class WebClientConfig {
    @Bean
    @LoadBalanced
    public WebClient.Builder builder() {
        return WebClient.builder();
    }
}
