package cn.tzq0301.auth.login.strategy;

import cn.tzq0301.auth.entity.user.User;
import cn.tzq0301.auth.infrastructure.UserInfrastructure;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final UserInfrastructure userInfrastructure;

    private final PasswordEncoder passwordEncoder;

    public LoginByIdentityStrategy(UserInfrastructure userInfrastructure, PasswordEncoder passwordEncoder) {
        this.userInfrastructure = userInfrastructure;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Mono<User> login(String identity, String password) {
        Mono<User> user = userInfrastructure.findByIdentity(identity);

        if (user == null) {
            return Mono.empty();
        }

        return user.filter(u -> passwordEncoder.matches(password, u.getPassword()));
    }
}
