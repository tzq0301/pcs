package cn.tzq0301.auth.login.service;

import cn.tzq0301.auth.entity.user.User;
import cn.tzq0301.auth.entity.user.Users;
import cn.tzq0301.auth.user.UserInfrastructure;
import lombok.AllArgsConstructor;
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

    public Mono<User> findByUserId(String userId) {
        return userInfrastructure.findByUserId(userId);
    }

    public Mono<User> findByIdentity(String identity) {
        return userInfrastructure.findByIdentity(identity);
    }

    public Mono<User> findByPhone(String phone) {
        return userInfrastructure.findByPhone(phone);
    }

    public Mono<User> saveUser(User user) {
        return userInfrastructure.saveUser(Users.addRolePrefix(user));
    }
}
