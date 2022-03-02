package cn.tzq0301.auth.login.strategy;

import cn.tzq0301.auth.entity.user.User;
import cn.tzq0301.auth.user.UserInfrastructure;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * 使用 ID 和密码进行登录
 *
 * @author tzq0301
 * @version 1.0
 */
@Component
@AllArgsConstructor
public class LoginByUserIdStrategy implements LoginStrategy {
    private final UserInfrastructure userInfrastructure;

    @Override
    public Mono<User> login(String userId, String password) {
        Mono<User> user = userInfrastructure.findByUserId(userId);

        if (user == null) {
            return Mono.empty();
        }

        return user;
    }
}
