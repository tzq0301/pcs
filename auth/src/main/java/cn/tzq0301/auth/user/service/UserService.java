package cn.tzq0301.auth.user.service;

import cn.tzq0301.auth.user.entity.User;
import cn.tzq0301.auth.user.infrastraction.UserInfrastructure;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * @author tzq0301
 * @version 1.0
 */
@Service
@AllArgsConstructor
public class UserService {
    private final UserInfrastructure userInfrastructure;

    public Mono<Boolean> isPhoneInEnduranceContainer(final String phone) {
        return userInfrastructure.findByPhone(phone)
                .map(user -> user != null ? Boolean.TRUE : Boolean.FALSE);
    }

    public Mono<User> findByUserId(String userId) {
        return userInfrastructure.findByUserId(userId);
    }

    public Mono<User> saveUser(User user) {
        return userInfrastructure.saveUser(user);
    }
}
