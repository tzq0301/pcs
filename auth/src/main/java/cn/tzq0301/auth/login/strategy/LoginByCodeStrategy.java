package cn.tzq0301.auth.login.strategy;

import cn.tzq0301.auth.entity.user.User;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * 使用手机号和短信验证码进行登录
 *
 * @author tzq0301
 * @version 1.0
 */
@Component
public class LoginByCodeStrategy implements LoginStrategy {
    @Override
    public Mono<User> login(String principal, String certification) {
        return null;
    }
}
