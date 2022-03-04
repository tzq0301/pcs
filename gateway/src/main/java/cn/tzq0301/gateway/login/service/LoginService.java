package cn.tzq0301.gateway.login.service;

import cn.tzq0301.gateway.login.manager.LoginManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static cn.tzq0301.gateway.config.RedisConfig.SMS_NAMESPACE_PREFIX;

/**
 * @author tzq0301
 * @version 1.0
 */
@Service
@AllArgsConstructor
public class LoginService {
    private final LoginManager loginManager;

    public Mono<Boolean> isValidationCodeConsistentWithPhone(final String phone, final String code) {
        return loginManager.isValidationCodeConsistentWithPhone(phone, code);
    }
}
