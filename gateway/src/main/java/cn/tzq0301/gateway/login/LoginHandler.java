package cn.tzq0301.gateway.login;

import cn.tzq0301.gateway.login.entity.LoginResponse;
import cn.tzq0301.gateway.login.entity.LoginResponseCode;
import cn.tzq0301.gateway.security.PcsUserDetailsService;
import cn.tzq0301.result.Result;
import cn.tzq0301.util.JWTUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpHeaders;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

/**
 * @author tzq0301
 * @version 1.0
 */
@Component
@AllArgsConstructor
@Log4j2
public class LoginHandler {
    private final PcsUserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;

    private final ReactiveRedisTemplate<String, Object> redisTemplate;

    private static final String ROLE_PREFIX = "ROLE_";

    /**
     * 使用手机号/身份证/学号与密码进行登录
     *
     * @param request 请求
     * @return 响应
     */
    public Mono<ServerResponse> loginByAccount(ServerRequest request) {
        String account = request.pathVariable("account");
        String password = request.pathVariable("password");

        return userDetailsService.findByUsername(account)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .doOnNext(user -> log.info("{} {} login", user.getAuthorities(), user.getUsername()))
                .map(user -> new LoginResponse(user.getUsername(),
                        user.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .findAny()
                                .map(role -> role.startsWith(ROLE_PREFIX) ? role.substring(ROLE_PREFIX.length()) : role)
                                .orElse("")))
                .flatMap(user -> Mono.just(JWTUtils.generateToken(user.getId(), user.getRole()))
                        .publishOn(Schedulers.boundedElastic())
                        .doOnNext(jwt -> log.info("Create jwt for {}: {}", user.getId(), jwt))
                        .doOnNext(jwt -> redisTemplate.opsForValue().set(jwt, "").subscribe())
                        .doOnNext(jwt -> redisTemplate.expire(jwt, Duration.ofMillis(JWTUtils.EXPIRATION)).subscribe())
                        .flatMap(jwt -> ServerResponse.ok()
                                .header(HttpHeaders.AUTHORIZATION, jwt)
                                .bodyValue(Result.success(
                                        user, LoginResponseCode.SUCCESS.getCode(), LoginResponseCode.SUCCESS.getMessage()))))
                .switchIfEmpty(ServerResponse.ok().bodyValue(Result.error(
                        LoginResponseCode.ERROR.getCode(), LoginResponseCode.ERROR.getMessage())));
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
