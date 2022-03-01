package cn.tzq0301.gateway.security;

import cn.tzq0301.util.JWTUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author tzq0301
 * @version 1.0
 */
@Component
@Log4j2
@AllArgsConstructor
public class AuthenticationManager implements ReactiveAuthenticationManager {
    private final ReactiveRedisTemplate<String, Object> redisTemplate;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.just(authentication)
                .filter(auth -> {
                    boolean authenticated = auth.isAuthenticated();
                    if (authenticated) {
                        log.info("'{}' has been authenticated", auth.getPrincipal());
                    } else {
                        log.info("'{}' has not been authenticated", auth.getPrincipal());
                    }
                    return !authenticated;
                })
                .switchIfEmpty(Mono.empty())
                .map(Authentication::getPrincipal)
                .map(principal -> {
                    log.info("Principal is {}", principal);
                    return principal.toString();
                })
                .publishOn(Schedulers.boundedElastic())
                .filter(jwt -> {
                    boolean isValid = redisTemplate.opsForValue().get(jwt).block() != null;
                    if (!isValid) {
                        log.warn("The following jwt is invalid: {}", jwt);
                    }
                    return isValid;
                })
                .switchIfEmpty(Mono.empty())
                .filter(jwt -> {
                    boolean tokenExpired = JWTUtils.isTokenExpired(jwt);
                    if (tokenExpired) {
                        log.info("'{}' has been expired", jwt);
                    } else {
                        log.info("'{}' has not been expired", jwt);
                    }
                    return !tokenExpired;
                })
                .switchIfEmpty(Mono.empty())
                .map(jwt -> {
                    String userId = JWTUtils.extractUserId(jwt);
                    String role = JWTUtils.extractUserRole(jwt);
                    log.info("[{}] {} authenticated", userId, role);
                    return new UsernamePasswordAuthenticationToken(
                            userId, null, Stream.of(role)
                            .map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
                });
    }
}
