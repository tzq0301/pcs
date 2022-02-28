package cn.tzq0301.auth.service;

import cn.tzq0301.auth.entity.user.Users;
import cn.tzq0301.auth.infrastructure.UserInfrastructure;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * @author tzq0301
 * @version 1.0
 */
@Service
public class UserService implements ReactiveUserDetailsService {
    private final UserInfrastructure userInfrastructure;

    public UserService(UserInfrastructure userInfrastructure) {
        this.userInfrastructure = userInfrastructure;
    }

    @Override
    public Mono<UserDetails> findByUsername(String userId) {
        return userInfrastructure.findByUserId(userId).map(Users::userToUserDetails);
    }
}
