package cn.tzq0301.auth.login.handler;

import cn.tzq0301.auth.entity.user.Users;
import cn.tzq0301.auth.login.service.LoginService;
import cn.tzq0301.auth.user.entity.UserResponse;
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

    public Mono<ServerResponse> getUserByAccount(ServerRequest request) {
        return Mono.just(request.pathVariable("account"))
                .flatMap(account -> {
                    if (Users.isIdentity(account)) {
                        return loginService.findByIdentity(account);
                    } else if (Users.isPhone(account)) {
                        return loginService.findByPhone(account);
                    } else {
                        return loginService.findByUserId(account);
                    }
                })
                .map(user -> new UserResponse(
                        user.getUserId(), user.getPassword(), user.getEnabled(), user.getRole()))
                .flatMap(ServerResponse.ok()::bodyValue);
    }
}
