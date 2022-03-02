package cn.tzq0301.gateway.login.handler;

import cn.tzq0301.gateway.config.RedisConfig;
import cn.tzq0301.gateway.login.entity.LoginResponse;
import cn.tzq0301.gateway.security.PcsUserDetailsService;
import cn.tzq0301.result.Result;
import cn.tzq0301.util.JWTUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

import static cn.tzq0301.gateway.login.entity.LoginResponseCode.*;

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
                .map(this::generateJwtResponse)
                .doOnNext(this::pushJwtToRedis)
                .flatMap(loginResponse -> ServerResponse.ok().bodyValue(
                        Result.success(loginResponse, SUCCESS.getCode(), SUCCESS.getMessage())))
                .switchIfEmpty(ServerResponse.ok().bodyValue(
                        Result.error(ERROR.getCode(), ERROR.getMessage())));
    }

    // FIXME
    public Mono<ServerResponse> requestMessageValidationCode(ServerRequest request) {
        final String phone = request.pathVariable("phone");
        final String code = request.pathVariable("code");

        return null;
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

    /**
     * 基于用户信息（用户 ID 与角色）生成 JWT
     *
     * @param user {@link org.springframework.security.core.userdetails.UserDetails}
     * @return {@link cn.tzq0301.gateway.login.entity.LoginResponse}
     */
    private LoginResponse generateJwtResponse(UserDetails user) {
        String userId = user.getUsername();
        String role = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findAny()
                .map(it -> it.startsWith(ROLE_PREFIX) ? it.substring(ROLE_PREFIX.length()) : it)
                .orElse("");

        String jwt = JWTUtils.generateToken(userId, role);
        log.info("Create jwt for {}: {}", userId, jwt);

        return new LoginResponse(userId, jwt, role);
    }

    /**
     * 将 JWT 放入 Redis 中
     *
     * @param loginResponse {@link cn.tzq0301.gateway.login.entity.LoginResponse}
     */
    private void pushJwtToRedis(LoginResponse loginResponse) {
        Mono.just(loginResponse)
                .publishOn(Schedulers.boundedElastic())
                .doOnNext(it -> redisTemplate.opsForValue()
                        .set(RedisConfig.JWT_NAMESPACE_PREFIX + it.getJwt(), "").subscribe())
                .doOnNext(it -> log.info("Push JWT to Redis: {}", it.getJwt()))
                .doOnNext(it -> redisTemplate
                        .expire(RedisConfig.JWT_NAMESPACE_PREFIX + it.getJwt(),
                                Duration.ofMillis(JWTUtils.EXPIRATION)).subscribe())
                .doOnNext(it -> log.info("Set expiration {} Millis for {}", JWTUtils.EXPIRATION, it.getJwt()))
                .subscribe();
    }
}
