package cn.tzq0301.auth.entity.user;

import cn.tzq0301.auth.login.entity.LoginResponse;
import cn.tzq0301.auth.login.entity.LoginResponseCode;
import cn.tzq0301.result.Result;
import com.google.common.base.Strings;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author tzq0301
 * @version 1.0
 */
public final class Users {
    private Users() {}

    public static boolean isIdentity(String account) {
        return Strings.nullToEmpty(account).length() == 18;
    }

    public static boolean isPhone(String account) {
        return Strings.nullToEmpty(account).length() == 11;
    }

    public static UserDetails userToUserDetails(User user) {
        return new org.springframework.security.core.userdetails.User(
                user.getUserId(), user.getPassword(), user.getEnable(), true, true, true,
                Stream.of(new SimpleGrantedAuthority(user.getRole().getRole())).collect(Collectors.toList()));
    }

    public static Result<LoginResponse> userToLoginResponse(User user) {
        return Result.success(new LoginResponse(user.getUserId(), user.getName(), user.getRole().getRole()),
                LoginResponseCode.SUCCESS.getCode(), LoginResponseCode.SUCCESS.getMessage());
    }

    public static void encoderPassword(User user, PasswordEncoder passwordEncoder) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    }
}
