package cn.tzq0301.auth.user.handler;

import cn.tzq0301.result.Result;
import cn.tzq0301.util.JWTUtils;
import com.google.common.base.Strings;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.Objects;

import static cn.tzq0301.auth.user.entity.UserResultEnum.USER_ID_NOT_MATCH;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON;

/**
 * @author tzq0301
 * @version 1.0
 */
@Aspect
@Component
public class UserHandlerAOP {
    private static final Logger log = LoggerFactory.getLogger(UserHandler.class);

    @Pointcut(value = "execution(public * cn.tzq0301.auth.user.handler.UserHandler.isPhoneInEnduranceContainer())")
    public void isPhoneInEnduranceContainer() {}

    @Pointcut(value = "execution(public * cn.tzq0301.auth.user.handler.UserHandler.getUserInformation())")
    public void getUserInformation() {}

    @Before(value = "isPhoneInEnduranceContainer()")
    public void beforeIsPhoneInEnduranceContainer(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();

        ServerRequest request = (ServerRequest) args[0];

        log.info("{} try to validate whether it is in endurance container", request.pathVariable("phone"));
    }

    @Before(value = "getUserInformation()")
    public void beforeGetUserInformation(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        ServerRequest request = (ServerRequest) args[0];

        String auth = request.headers().firstHeader(AUTHORIZATION);
        if (Strings.isNullOrEmpty(auth)) {
            return;
        }

        String jwt = auth.substring(JWTUtils.AUTHORIZATION_HEADER_PREFIX.length());
        String userId = request.pathVariable("user_id");

        log.info("{} tries to get information of user {}", JWTUtils.extractUserId(jwt), userId);
    }
}
