package cn.tzq0301.auth.login;

import cn.tzq0301.auth.entity.user.User;
import cn.tzq0301.auth.entity.user.Users;
import cn.tzq0301.auth.login.entity.LoginResponseCode;
import cn.tzq0301.auth.login.strategy.LoginByCodeStrategy;
import cn.tzq0301.auth.login.strategy.LoginByIdentityStrategy;
import cn.tzq0301.auth.login.strategy.LoginByPhoneStrategy;
import cn.tzq0301.auth.login.strategy.LoginByUserIdStrategy;
import cn.tzq0301.result.Result;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * @author tzq0301
 * @version 1.0
 */
@Component
@AllArgsConstructor
public class LoginHandler {
    private final LoginService loginService;

    private final LoginByCodeStrategy loginByCodeStrategy;

    private final LoginByIdentityStrategy loginByIdentityStrategy;

    private final LoginByPhoneStrategy loginByPhoneStrategy;

    private final LoginByUserIdStrategy loginByUserIdStrategy;

    /**
     * 使用手机号/身份证/学号与密码进行登录
     *
     * @param request 请求
     * @return 响应
     */
    public Mono<ServerResponse> loginByAccount(ServerRequest request) {
        String account = request.pathVariable("account");
        String password = request.pathVariable("password");

        Mono<User> user;
        if (Users.isIdentity(account)) {
            user = loginService.login(loginByIdentityStrategy, account, password);
        } else if (Users.isPhone(account)) {
            user = loginService.login(loginByPhoneStrategy, account, password);
        } else {
            user = loginService.login(loginByUserIdStrategy, account, password);
        }

        return user
                .flatMap(u -> ServerResponse.ok().bodyValue(Users.userToLoginResponse(u)))
                .switchIfEmpty(ServerResponse.ok().bodyValue(
                        Result.error(LoginResponseCode.ERROR.getCode(), LoginResponseCode.ERROR.getMessage())));
    }

    /**
     * 使用手机号与短信验证码进行登录
     *
     * @param request 请求
     * @return 响应
     */
    public Mono<ServerResponse> loginByCode(ServerRequest request) {
        return null;
    }
}
