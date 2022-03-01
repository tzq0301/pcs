package cn.tzq0301.gateway.filter;

import cn.tzq0301.gateway.security.SecurityContextRepository;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpHeaders;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

/**
 * @author tzq0301
 * @version 1.0
 */
@Component
@AllArgsConstructor
@Log4j2
public class JWTFilter implements GlobalFilter, Ordered {
    private final ReactiveRedisTemplate<String, Object> redisTemplate;

    private static final String LOGIN_ROUTE_PREFIX = "/login";

    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("HTTP Request -> URI: {}, Authorization Headers: {}",
                exchange.getRequest().getURI(),
                exchange.getRequest().getHeaders().get(org.springframework.http.HttpHeaders.AUTHORIZATION));

        if (isLoginRoute(exchange)) {
            return chain.filter(exchange);
        }

        if (!hasAuthorized(exchange)) {
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        return chain.filter(exchange);
    }

    private boolean isLoginRoute(ServerWebExchange exchange) {
        return exchange.getRequest().getURI().toString().startsWith(LOGIN_ROUTE_PREFIX);
    }

    private boolean hasAuthorized(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (Strings.isNullOrEmpty(authHeader)) {
            return false;
        }

        if (!authHeader.startsWith(BEARER_PREFIX)) {
            return false;
        }

        String jwt = authHeader.substring(7);

        if (redisTemplate.opsForValue().get(jwt).block() == null) {
            return false;
        }

        return true;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}