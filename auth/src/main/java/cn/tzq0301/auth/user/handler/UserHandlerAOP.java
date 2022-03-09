package cn.tzq0301.auth.user.handler;

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

import java.util.Objects;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

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

    @Pointcut(value = "execution(public * cn.tzq0301.auth.user.handler.UserHandler.updatePassword())")
    public void updatePassword() {}

    @Before(value = "isPhoneInEnduranceContainer()")
    public void beforeIsPhoneInEnduranceContainer(JoinPoint joinPoint) {
        ServerRequest request = getRequest(joinPoint);

        log.info("{} try to validate whether it is in endurance container", request.pathVariable("phone"));
    }

    @Before(value = "getUserInformation()")
    public void beforeGetUserInformation(JoinPoint joinPoint) {
        ServerRequest request = getRequest(joinPoint);

        if (hasAuthorization(request)) {
            return;
        }

        String jwt = getJWT(request);
        String userId = request.pathVariable("user_id");

        log.info("{} tries to get information of user {}", JWTUtils.extractUserId(jwt), userId);
    }

    @Before(value = "updatePassword()")
    public void beforeUpdatePassword(JoinPoint joinPoint) {
        ServerRequest request = getRequest(joinPoint);

        if (hasAuthorization(request)) {
            return;
        }

        String jwt = getJWT(request);
        String userId = request.pathVariable("user_id");

        log.info("{} tries to update password of user {}", JWTUtils.extractUserId(jwt), userId);
    }

    private static ServerRequest getRequest(JoinPoint joinPoint) {
        return (ServerRequest) joinPoint.getArgs()[0];
    }

    private static boolean hasAuthorization(ServerRequest request) {
        return Strings.isNullOrEmpty(request.headers().firstHeader(AUTHORIZATION));
    }

    private static String getJWT(ServerRequest request) {
        return Objects.requireNonNull(request.headers().firstHeader(AUTHORIZATION))
                .substring(JWTUtils.AUTHORIZATION_HEADER_PREFIX.length());
    }
}
