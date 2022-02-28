package cn.tzq0301.auth.login.strategy;

import cn.tzq0301.auth.entity.user.User;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * 使用身份证和密码进行登录
 *
 * @author tzq0301
 * @version 1.0
 */
@Service
public class LoginByIdentityStrategy implements LoginStrategy {
    @Override
    public Mono<User> login(String principal, String certification) {
        return null;
    }
}