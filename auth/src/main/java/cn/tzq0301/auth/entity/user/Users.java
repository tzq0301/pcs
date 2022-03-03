package cn.tzq0301.auth.entity.user;

import cn.tzq0301.auth.login.entity.LoginResponse;
import cn.tzq0301.auth.login.entity.LoginResponseCode;
import cn.tzq0301.result.Result;
import com.google.common.base.Strings;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Objects;

import static cn.tzq0301.auth.login.entity.LoginResponseCode.SUCCESS;

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

    public static Result<LoginResponse> userToLoginResponse(User user) {
        Objects.requireNonNull(user);

        return Result.success(new LoginResponse(user.getUserId(), user.getName(), user.getRole()), SUCCESS);
    }

    public static User addRolePrefix(User user) {
        Objects.requireNonNull(user);

        if (user.getRole().startsWith(ROLE_PREFIX)) {
            return user;
        }

        user.setRole(ROLE_PREFIX + user.getRole());

        return user;
    }

    private Users() {}
}
