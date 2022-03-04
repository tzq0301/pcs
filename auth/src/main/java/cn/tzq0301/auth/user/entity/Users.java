package cn.tzq0301.auth.user.entity;

import cn.tzq0301.auth.login.entity.LoginResponse;
import cn.tzq0301.auth.login.entity.LoginResponseCode;
import cn.tzq0301.result.Result;
import cn.tzq0301.user.Sex;
import cn.tzq0301.util.DateUtils;
import com.google.common.base.Strings;

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
