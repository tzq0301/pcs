package cn.tzq0301.auth.user.service;

import cn.tzq0301.auth.user.entity.User;
import cn.tzq0301.auth.user.entity.UserInfo;
import cn.tzq0301.auth.user.entity.vo.UserInfoVO;
import cn.tzq0301.auth.user.infrastraction.UserInfrastructure;
import cn.tzq0301.util.DateUtils;
import cn.tzq0301.util.SexUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

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

    public Mono<Integer> isUserAbleToApply(String userId) {
        return userInfrastructure.findByUserId(userId).map(User::getStudentStatus);
    }

    public Mono<User> setStudentStatus(User user, int studentStatus) {
        user.setStudentStatus(studentStatus);
        return userInfrastructure.saveUser(user);
    }

    public Mono<List<UserInfoVO>> listAllUsers() {
        return userInfrastructure.listAllUsers()
                .map(user -> new UserInfoVO(user.getUserId(), user.getName(), user.getRole(),
                        SexUtils.sexOfString(user.getSex()), DateUtils.localDateToString(user.getBirthday()),
                        user.getPhone(), user.getEmail(), user.getIdentity()))
                .collectList();
    }

    public Mono<List<UserInfoVO>> listAllUsersByRole(final String role) {
        return userInfrastructure.listAllUsersByRole(role)
                .map(user -> new UserInfoVO(user.getUserId(), user.getName(), user.getRole(),
                        SexUtils.sexOfString(user.getSex()), DateUtils.localDateToString(user.getBirthday()),
                        user.getPhone(), user.getEmail(), user.getIdentity()))
                .collectList();
    }

    public Mono<User> deleteUserByUserId(final String userId) {
        return userInfrastructure.findByUserId(userId)
                .map(user -> {
                    user.setEnabled(Boolean.FALSE);
                    return user;
                })
                .flatMap(userInfrastructure::saveUser);
    }
}
