package cn.tzq0301.auth.user.entity;

import cn.tzq0301.auth.login.entity.LoginResponse;
import cn.tzq0301.auth.login.entity.LoginResponseCode;
import cn.tzq0301.result.Result;
import cn.tzq0301.user.Role;
import cn.tzq0301.user.Sex;
import cn.tzq0301.util.DateUtils;
import cn.tzq0301.util.SexUtils;
import com.google.common.base.Strings;

import java.time.LocalDate;
import java.util.Objects;

/**
 * @author tzq0301
 * @version 1.0
 */
public final class Users {
    private static final String ROLE_PREFIX = "ROLE_";

    public static boolean isIdentity(String account) {
        return Strings.nullToEmpty(account).length() == 18;
    }

    public static boolean isPhone(String account) {
        return Strings.nullToEmpty(account).length() == 11;
    }

    public static User newUser(String userId, String name, String password,
                               String role, Integer sex, LocalDate birthday,
                               String phone, String email, String identity) {
        return new User(userId, name, password, true, role, sex, birthday, phone, email, identity, 0);
    }

    public static User newUser(String userId, String name, String password,
                               Role role, Integer sex, LocalDate birthday,
                               String phone, String email, String identity) {
        return new User(userId, name, password, true, role.getRole(), sex, birthday, phone, email, identity, 0);
    }

    public static User newUser(String userId, String name, String password,
                               String role, Sex sex, LocalDate birthday,
                               String phone, String email, String identity) {
        return new User(userId, name, password, true, role, sex.getSex(), birthday, phone, email, identity, 0);
    }

    public static User newUser(String userId, String name, String password,
                               Role role, Sex sex, LocalDate birthday,
                               String phone, String email, String identity) {
        return new User(userId, name, password, true, role.getRole(), sex.getSex(), birthday, phone, email, identity, 0);
    }

    public static User newUser(String userId, String name, String password,
                               String role, String sex, LocalDate birthday,
                               String phone, String email, String identity) {
        return new User(userId, name, password, true, role, SexUtils.sexToInteger(sex), birthday, phone, email, identity, 0);
    }

    @Deprecated
    public static Result<LoginResponse> userToLoginResponse(User user) {
        Objects.requireNonNull(user);

        return Result.success(new LoginResponse(user.getUserId(), user.getName(), user.getRole()), LoginResponseCode.SUCCESS);
    }

    public static User addRolePrefix(User user) {
        Objects.requireNonNull(user);

        if (user.getRole().startsWith(ROLE_PREFIX)) {
            return user;
        }

        user.setRole(ROLE_PREFIX + user.getRole());

        return user;
    }

    public static void removeRolePrefix(User user) {
        Objects.requireNonNull(user);

        if (user.getRole().startsWith(ROLE_PREFIX)) {
            user.setRole(user.getRole().substring(ROLE_PREFIX.length()));
        }
    }

    public static UserInfoResponse userToUserInfoResponse(User user) {
        Objects.requireNonNull(user);

        removeRolePrefix(user);

        return new UserInfoResponse(
                user.getName(), user.getRole(),
                Objects.requireNonNull(Sex.from(user.getSex())).getStr(),
                DateUtils.localDateToString(user.getBirthday()),
                user.getPhone(), user.getEmail(), user.getIdentity());
    }

    private Users() {}
}
