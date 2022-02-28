package cn.tzq0301.auth.infrastructure;

import cn.tzq0301.auth.entity.user.User;
import cn.tzq0301.auth.repository.UserRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * @author tzq0301
 * @version 1.0
 */
@Repository
public class UserInfrastructure {
    private final UserRepository userRepository;

    public UserInfrastructure(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Mono<User> findByUserId(String userId) {
        return userRepository.findByUserId(userId);
    }

    public Mono<User> findByIdentity(String account) {
        return userRepository.findByIdentity(account);
    }

    public Mono<User> saveUser(User user) {
        return userRepository.save(user);
    }
}
