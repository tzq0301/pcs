package cn.tzq0301.gateway.security;

import cn.tzq0301.gateway.config.RedisConfig;
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

    private static final String ROLE_PREFIX = "ROLE_";

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.just(authentication)
                .filter(AuthenticationManager::isAuthenticated)
                .switchIfEmpty(Mono.empty())

                .map(Authentication::getPrincipal)
                .map(Object::toString)
                .doOnNext(it -> log.info("Principal is {}", it))

                .publishOn(Schedulers.boundedElastic())

                .filter(this::isJwtValid)
                .switchIfEmpty(Mono.empty())

                .filter(AuthenticationManager::isJwtExpired)
                .switchIfEmpty(Mono.empty())

                .map(AuthenticationManager::jwtToAuthentication);
    }

    private static boolean isAuthenticated(final Authentication authentication) {
        boolean authenticated = authentication.isAuthenticated();

        if (authenticated) {
            log.info("'{}' has been authenticated", authentication.getPrincipal());
        } else {
            log.info("'{}' has not been authenticated", authentication.getPrincipal());
        }

        return !authenticated;
    }

    private boolean isJwtValid(final String jwt) {
        boolean isValid = redisTemplate.opsForValue().get(RedisConfig.JWT_NAMESPACE_PREFIX + jwt).block() != null;

        if (!isValid) {
            log.warn("The following jwt is invalid: {}", jwt);
        }

        return isValid;
    }

    private static boolean isJwtExpired(final String jwt) {
        boolean tokenExpired = JWTUtils.isTokenExpired(jwt);

        if (tokenExpired) {
            log.info("'{}' has been expired", jwt);
        } else {
            log.info("'{}' has not been expired", jwt);
        }

        return !tokenExpired;
    }

    private static Authentication jwtToAuthentication(final String jwt) {
        String userId = JWTUtils.extractUserId(jwt);
        String role = JWTUtils.extractUserRole(jwt);

        log.info("[{}] {} authenticated", userId, role);

        return new UsernamePasswordAuthenticationToken(userId, null, Stream.of(role)
                .map(r -> r.startsWith(ROLE_PREFIX) ? r : ROLE_PREFIX + r)
                .map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
    }
}
