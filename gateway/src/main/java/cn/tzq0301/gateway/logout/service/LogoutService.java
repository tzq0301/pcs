package cn.tzq0301.gateway.logout.service;

import cn.tzq0301.gateway.logout.manager.LogoutManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * @author tzq0301
 * @version 1.0
 */
@Service
@AllArgsConstructor
public class LogoutService {
    private final LogoutManager logoutManager;

    public Mono<Void> logout(final String jwt) {
        return logoutManager.logout(jwt);
    }
}
