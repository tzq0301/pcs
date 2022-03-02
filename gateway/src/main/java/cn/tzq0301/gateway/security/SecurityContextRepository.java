package cn.tzq0301.gateway.security;

import cn.tzq0301.util.JWTUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author tzq0301
 * @version 1.0
 */
@Component
@AllArgsConstructor
@Log4j2
public class SecurityContextRepository implements ServerSecurityContextRepository {
    private final ReactiveAuthenticationManager authenticationManager;

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        return Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
                .filter(auth -> auth.startsWith(JWTUtils.AUTHORIZATION_HEADER_PREFIX))
                .map(auth -> auth.substring(JWTUtils.AUTHORIZATION_HEADER_PREFIX.length()))
                .map(auth -> new UsernamePasswordAuthenticationToken(auth, auth))
                .flatMap(authenticationManager::authenticate)
                .map(SecurityContextImpl::new);
    }
}
