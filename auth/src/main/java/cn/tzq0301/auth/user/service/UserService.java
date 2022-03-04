package cn.tzq0301.auth.user.service;

import cn.tzq0301.auth.user.entity.User;
import cn.tzq0301.auth.user.infrastraction.UserInfrastructure;
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
public class UserService {
    private final UserInfrastructure userInfrastructure;

    private final PasswordEncoder passwordEncoder;

    public Mono<Boolean> isPhoneInEnduranceContainer(final String phone) {
        return userInfrastructure.findByPhone(phone)
                .map(user -> user != null ? Boolean.TRUE : Boolean.FALSE);
    }

    public Mono<User> findByUserId(String userId) {
        return userInfrastructure.findByUserId(userId);
    }

    public Mono<User> updateUser(User user) {
        return userInfrastructure.saveUser(user);
    }

    public Mono<User> saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userInfrastructure.saveUser(user);
    }
}
