package cn.tzq0301.auth.user;

import cn.tzq0301.auth.entity.user.User;
import cn.tzq0301.auth.entity.user.Users;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * @author tzq0301
 * @version 1.0
 */
@Service
public class UserService {
    private final UserInfrastructure userInfrastructure;

    public UserService(UserInfrastructure userInfrastructure) {
        this.userInfrastructure = userInfrastructure;
    }

    public Mono<User> findByUserId(String userId) {
        return userInfrastructure.findByUserId(userId);
    }

    public Mono<User> saveUser(User user) {
        return userInfrastructure.saveUser(Users.addRolePrefix(user));
    }
}
