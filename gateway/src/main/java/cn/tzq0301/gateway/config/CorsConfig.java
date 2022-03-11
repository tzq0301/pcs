package cn.tzq0301.gateway.config;

import com.alibaba.nacos.common.utils.HttpMethod;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.parameters.P;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

import static com.alibaba.nacos.common.utils.HttpMethod.*;

/**
 * @author tzq0301
 * @version 1.0
 */
@Configuration
public class CorsConfig {
    @Bean
    public CorsWebFilter corsWebFilter() {
        final CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(Collections.singletonList("*"));
        corsConfig.setMaxAge(3600L);
        corsConfig.setAllowedMethods(Collections.singletonList("*"));
        corsConfig.addAllowedHeader("*");

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }
}
