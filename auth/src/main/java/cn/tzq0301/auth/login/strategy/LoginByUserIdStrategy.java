package cn.tzq0301.auth.login.strategy;

import cn.tzq0301.auth.entity.user.User;
import cn.tzq0301.auth.infrastructure.UserInfrastructure;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * 使用 ID 和密码进行登录
 *
 * @author tzq0301
 * @version 1.0
 */
@Component
public class LoginByUserIdStrategy implements LoginStrategy {
    private final UserInfrastructure userInfrastructure;

    private final PasswordEncoder passwordEncoder;

    public LoginByUserIdStrategy(
            UserInfrastructure userInfrastructure,
            PasswordEncoder passwordEncoder) {
        this.userInfrastructure = userInfrastructure;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Mono<User> login(String userId, String password) {
        Mono<User> user = userInfrastructure.findByUserId(userId);

        if (user == null) {
            return Mono.empty();
        }

        return user.filter(u -> passwordEncoder.matches(password, u.getPassword()));
    }
}
