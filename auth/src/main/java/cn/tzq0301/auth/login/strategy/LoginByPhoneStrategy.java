package cn.tzq0301.auth.login.strategy;

import cn.tzq0301.auth.entity.user.User;
import cn.tzq0301.auth.infrastructure.UserInfrastructure;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * 使用手机号和密码进行登录
 *
 * @author tzq0301
 * @version 1.0
 */
@Service
public class LoginByPhoneStrategy implements LoginStrategy {
    private final UserInfrastructure userInfrastructure;

    private final PasswordEncoder passwordEncoder;

    public LoginByPhoneStrategy(UserInfrastructure userInfrastructure, PasswordEncoder passwordEncoder) {
        this.userInfrastructure = userInfrastructure;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Mono<User> login(String phone, String password) {
        Mono<User> user = userInfrastructure.findByPhone(phone);

        if (user == null) {
            return Mono.empty();
        }

        return user.filter(u -> passwordEncoder.matches(password, u.getPassword()));
    }
}
