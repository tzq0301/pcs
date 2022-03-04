package cn.tzq0301.gateway.logout.handler;

import cn.tzq0301.util.JWTUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Optional;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * @author tzq0301
 * @version 1.0
 */
@Aspect
@Component
public class LogoutHandlerAOP {
    private static final Logger log = LoggerFactory.getLogger(LogoutHandler.class);

    @Pointcut("execution(public * cn.tzq0301.gateway.logout.handler.LogoutHandler.logout())")
    public void logout() {}

    @Before(value = "logout()")
    public void beforeLogout(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        ServerRequest request = (ServerRequest) args[0];
        log.info("User who hold the following jwt requires to logout: {}",
                Optional.ofNullable(request.headers().firstHeader(AUTHORIZATION))
                        .orElse("no jwt")
                        .substring(JWTUtils.AUTHORIZATION_HEADER_PREFIX.length()));
    }
}
