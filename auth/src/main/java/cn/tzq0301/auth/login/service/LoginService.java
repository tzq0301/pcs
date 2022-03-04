package cn.tzq0301.auth.login.service;

import cn.tzq0301.auth.user.entity.User;
import cn.tzq0301.auth.user.entity.Users;
import cn.tzq0301.auth.user.infrastraction.UserInfrastructure;
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
