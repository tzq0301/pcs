package cn.tzq0301.auth.user;

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
public class UserHandler {
    private final UserService userService;

    public Mono<ServerResponse> getUserByUserId(ServerRequest request) {
        return userService
                .findByUserId(request.pathVariable("user_id"))
                .map(user -> new UserResponse(
                        user.getUserId(), user.getPassword(), user.getEnabled(), user.getRole()))
                .flatMap(ServerResponse.ok()::bodyValue);
    }
}
