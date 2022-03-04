package cn.tzq0301.auth.user.handler;

import cn.tzq0301.auth.user.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
@Log4j2
public class UserHandler {
    private final UserService userService;

    public Mono<ServerResponse> isPhoneInEnduranceContainer(ServerRequest request) {
        String phone = request.pathVariable("phone");
        log.info("{} try to validate whether it is in endurance container", phone);

        return userService.isPhoneInEnduranceContainer(phone)
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> getUserInformation(ServerRequest request) {
        return null;
    }
}
