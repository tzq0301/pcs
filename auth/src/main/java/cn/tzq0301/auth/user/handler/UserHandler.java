package cn.tzq0301.auth.user.handler;

import cn.tzq0301.auth.user.entity.User;
import cn.tzq0301.auth.user.entity.vo.ImportStudentInfo;
import cn.tzq0301.auth.user.entity.vo.UserResultEnum;
import cn.tzq0301.auth.user.entity.Users;
import cn.tzq0301.auth.user.service.UserService;
import cn.tzq0301.entity.RecordsWithTotal;
import cn.tzq0301.result.Result;
import cn.tzq0301.util.DateUtils;
import cn.tzq0301.util.JWTUtils;
import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static cn.tzq0301.auth.user.entity.vo.UserResultEnum.*;
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

    private final PasswordEncoder passwordEncoder;

    public Mono<ServerResponse> isPhoneInEnduranceContainer(ServerRequest request) {
        String phone = request.pathVariable("phone");

        return userService.isPhoneInEnduranceContainer(phone)
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> getUserInformation(ServerRequest request) {
        String userId = request.pathVariable("user_id");

        return checkForUserId(request, userId)
                .switchIfEmpty(userService
                        .findByUserId(userId)
                        .map(Users::userToUserInfoResponse)
                        .map(response -> Result.success(response, UserResultEnum.SUCCESS))
                        .flatMap(ServerResponse.ok()::bodyValue));
    }

    public Mono<ServerResponse> updateUserInformation(ServerRequest request) {
        String userId = request.pathVariable("user_id");

        return userService
                .findByUserId(userId)
                .doOnNext(user -> {
                    MultiValueMap<String, String> queryParams =
                            request.exchange().getRequest().getQueryParams();

                    String sex = queryParams.getFirst("sex");
                    if (!Strings.isNullOrEmpty(sex)) {
                        log.info("Update User Sex -> {}", sex);
                        user.setSex(Integer.parseInt(sex));
                    }

                    String birthday = queryParams.getFirst("birthday");
                    if (!Strings.isNullOrEmpty(birthday)) {
                        log.info("Update User birthday -> {}", birthday);
                        user.setBirthday(DateUtils.stringToLocalDate(birthday));
                    }

                    String phone = queryParams.getFirst("phone");
                    if (!Strings.isNullOrEmpty(phone)) {
                        log.info("Update User phone -> {}", phone);
                        user.setPhone(phone);
                    }

                    String email = queryParams.getFirst("email");
                    if (!Strings.isNullOrEmpty(email)) {
                        log.info("Update User email -> {}", email);
                        user.setEmail(email);
                    }
                })
                .flatMap(userService::updateUser)
                .flatMap(user -> ServerResponse.ok().bodyValue(Result.success(SUCCESS)));
    }

    public Mono<ServerResponse> updatePassword(ServerRequest request) {
        String userId = request.pathVariable("user_id");
        String oldPassword = request.pathVariable("old_password");
        String newPassword = request.pathVariable("new_password");

        return checkForUserId(request, userId)
                .switchIfEmpty(Mono.just(Objects.equals(oldPassword, newPassword))
                        .flatMap(equals -> {
                            if (equals) {
                                return ServerResponse.ok().bodyValue(Result.error(PASSWORD_DUPLICATE));
                            }

                            return userService.findByUserId(userId).flatMap(user -> {
                                if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
                                    return ServerResponse.ok().bodyValue(Result.error(OLD_PASSWORD_NOT_CORRECT));
                                }

                                user.setPassword(newPassword);
                                return userService.saveUser(user)
                                        .flatMap(u -> ServerResponse.ok().bodyValue(Result.success(SUCCESS)))
                                        .onErrorResume(ex -> ex instanceof Exception, it ->
                                                ServerResponse.ok().bodyValue(Result.error(UPDATE_FAIL)));
                            });
                        })
                );
    }

    private Mono<ServerResponse> checkForUserId(ServerRequest request, String userId) {
        String auth = request.headers().firstHeader(AUTHORIZATION);
        if (Strings.isNullOrEmpty(auth)) {
            return ServerResponse.status(UNAUTHORIZED).build();
        }

        String jwt = JWTUtils.getJwtFromAuthorizationHeader(auth);
        if (!Objects.equals(userId, JWTUtils.extractUserId(jwt))) {
            return ServerResponse.ok().contentType(APPLICATION_JSON).bodyValue(
                    Result.error(USER_ID_NOT_MATCH));
        }

        return Mono.empty();
    }

    public Mono<ServerResponse> isUserAbleToApply(ServerRequest request) {
        String userId = request.pathVariable("user_id");

        return userService.isUserAbleToApply(userId)
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> setStudentStatusToOne(ServerRequest request) {
        String userId = request.pathVariable("user_id");
        int studentStatus = Integer.parseInt(request.pathVariable("student_status"));

        return userService.findByUserId(userId)
                .flatMap(user -> userService.setStudentStatus(user, studentStatus))
                .map(User::getStudentStatus)
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> getUserInfoByJWT(ServerRequest request) {
        String userId = request.pathVariable("user_id");

        return checkForUserId(request, userId)
                .switchIfEmpty(userService
                        .findByUserId(userId)
                        .map(Users::userToUserInfo)
                        .flatMap(ServerResponse.ok()::bodyValue));
    }

    public Mono<ServerResponse> getUserInfo(ServerRequest request) {
        String userId = request.pathVariable("user_id");

        return userService
                .findByUserId(userId)
                .map(Users::userToUserInfo)
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> listUserInfos(ServerRequest request) {
        int offset = getOffset(request);
        int limit = getLimit(request);
        String role = getAttribute(request, "role");
        String name = getAttribute(request, "name");

        return Mono.defer(() -> Strings.isNullOrEmpty(role)
                ? userService.listAllUsers()
                : userService.listAllUsersByRole(role))
                .map(list -> new RecordsWithTotal<>(list, user -> user.getName().contains(name), offset, limit))
                .map(Result::success)
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> deleteUserByUserId(ServerRequest request) {
        return userService.deleteUserByUserId(request.pathVariable("user_id"))
                .flatMap(it -> ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> importUsers(ServerRequest request) {
        return userService.saveUsers(request.bodyToFlux(ImportStudentInfo.class))
                .map(it -> Result.success())
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> listUserInfosByRole(ServerRequest request) {
        return userService.listAllUsersByRole(request.pathVariable("role"))
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    /**
     * 获取请求参数中的 offset
     *
     * @return offset（默认值为 0）
     */
    private int getOffset(ServerRequest request) {
        String offset = request.exchange().getRequest().getQueryParams().getFirst("offset");

        if (Strings.isNullOrEmpty(offset)) {
            return 0;
        }

        return Integer.parseInt(offset);
    }

    /**
     * 获取请求参数中的 limit
     *
     * @return limit（默认值为 {@code Integer.MAX_VALUE}）
     */
    private int getLimit(ServerRequest request) {
        String limit = request.exchange().getRequest().getQueryParams().getFirst("limit");

        if (Strings.isNullOrEmpty(limit)) {
            return Integer.MAX_VALUE;
        }

        return Integer.parseInt(limit);
    }

    /**
     * 获取指定请求参数
     *
     * @return 指定
     */
    private String getAttribute(ServerRequest request, final String attribute) {
        return request.exchange().getRequest().getQueryParams().getFirst(attribute);
    }
}
