package cn.tzq0301.auth.user.service;

import cn.tzq0301.auth.user.entity.User;
import cn.tzq0301.auth.user.entity.Users;
import cn.tzq0301.auth.user.entity.vo.ImportStudentInfo;
import cn.tzq0301.auth.user.entity.vo.UserInfoVO;
import cn.tzq0301.auth.user.infrastraction.UserInfrastructure;
import cn.tzq0301.auth.user.manager.UserManager;
import cn.tzq0301.util.DateUtils;
import cn.tzq0301.util.SexUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
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
@RefreshScope
public class UserService {
    private final UserInfrastructure userInfrastructure;

    private final UserManager userManager;

    private final PasswordEncoder passwordEncoder;

    @Value("${auth.password}")
    private String defaultPassword;

    public UserService(
            UserInfrastructure userInfrastructure,
            UserManager userManager,
            PasswordEncoder passwordEncoder) {
        this.userInfrastructure = userInfrastructure;
        this.userManager = userManager;
        this.passwordEncoder = passwordEncoder;
    }

    public Mono<Boolean> isPhoneInEnduranceContainer(final String phone) {
        return userInfrastructure.findByPhone(phone)
                .map(user -> user != null ? Boolean.TRUE : Boolean.FALSE);
    }

    public Mono<User> findByUserId(final String userId) {
        return userManager.getUserByUserIdFromCache(userId)
                .switchIfEmpty(userInfrastructure.findByUserId(userId));
    }

    public Mono<User> updateUser(final User user) {
        return userInfrastructure.saveUser(user)
                .flatMap(userManager::putUserIdAndUserIntoCache);
    }

    public Mono<User> saveUser(final User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userInfrastructure.saveUser(user)
                .flatMap(userManager::putUserIdAndUserIntoCache);
    }

    public Mono<Integer> isUserAbleToApply(final String userId) {
        return userInfrastructure.findByUserId(userId).map(User::getStudentStatus);
    }

    public Mono<User> setStudentStatus(final User user, final int studentStatus) {
        user.setStudentStatus(studentStatus);
        return userInfrastructure.saveUser(user)
                .flatMap(userManager::putUserIdAndUserIntoCache);
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
                .flatMap(userInfrastructure::saveUser)
                .flatMap(userManager::putUserIdAndUserIntoCache);
    }

    public Mono<List<User>> saveUsers(final Flux<ImportStudentInfo> studentInfos) {
        return studentInfos
                .flatMap(userInfo -> {
                    String birthday = userInfo.getIdentity().substring(6, 14); // ?????????????????????????????????
                    return userInfrastructure.saveUser(Users.newUser(
                            userInfo.getId(), userInfo.getUsername(),
                            passwordEncoder.encode(defaultPassword),
                            userInfo.getRole(), userInfo.getSex(), DateUtils.stringToLocalDate(birthday),
                            userInfo.getPhone(), userInfo.getEmail(), userInfo.getIdentity()));
                })
                .collectList();
    }
}
