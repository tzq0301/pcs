package cn.tzq0301.auth.user.handler;

import cn.tzq0301.auth.user.entity.UserResultEnum;
import cn.tzq0301.auth.user.entity.Users;
import cn.tzq0301.auth.user.service.UserService;
import cn.tzq0301.result.Result;
import cn.tzq0301.util.JWTUtils;
import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static cn.tzq0301.auth.user.entity.UserResultEnum.USER_ID_NOT_MATCH;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON;

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

        return userService.isPhoneInEnduranceContainer(phone)
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> getUserInformation(ServerRequest request) {
        String auth = request.headers().firstHeader(AUTHORIZATION);
        if (Strings.isNullOrEmpty(auth)) {
            return ServerResponse.status(UNAUTHORIZED).build();
        }

        String jwt = JWTUtils.getJwtFromAuthorizationHeader(auth);
        String userId = request.pathVariable("user_id");
        if (!Objects.equals(userId, JWTUtils.extractUserId(jwt))) {
            return ServerResponse.ok().contentType(APPLICATION_JSON).bodyValue(
                    Result.error(USER_ID_NOT_MATCH));
        }

        return userService
                .findByUserId(userId)
                .map(Users::userToUserInfoResponse)
                .map(response -> Result.success(response, UserResultEnum.SUCCESS))
                .flatMap(ServerResponse.ok()::bodyValue);
    }


}
