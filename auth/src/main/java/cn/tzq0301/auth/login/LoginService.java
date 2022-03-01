package cn.tzq0301.auth.login;

import cn.tzq0301.auth.entity.user.User;
import cn.tzq0301.auth.entity.user.Users;
import cn.tzq0301.auth.user.UserInfrastructure;
import cn.tzq0301.auth.login.strategy.LoginStrategy;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * @author tzq0301
 * @version 1.0
 */
@Service
@AllArgsConstructor
public class LoginService {
    private final UserInfrastructure userInfrastructure;

    public Mono<User> login(LoginStrategy loginStrategy, String principal, String certification) {
        return loginStrategy.login(principal, certification);
    }

    public Mono<User> saveUser(User user) {
        return userInfrastructure.saveUser(user);
    }
}
