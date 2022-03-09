package cn.tzq0301.visit.apply.handler;

import cn.tzq0301.util.CommonUtils;
import cn.tzq0301.util.JWTUtils;
import com.google.common.base.Strings;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
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
public class ApplyHandlerAOP {
    private static final Logger log = LoggerFactory.getLogger(ApplyHandler.class);

    @Pointcut("execution(public * cn.tzq0301.visit.apply.handler.ApplyHandler.requestApply())")
    public void requestApply() {}

    @Pointcut("execution(public * cn.tzq0301.visit.apply.handler.ApplyHandler.deleteApplyById())")
    public void deleteApplyById() {}

    @Before(value = "requestApply()")
    public void beforeRequestApply(JoinPoint joinPoint) {
        ServerRequest request = getRequest(joinPoint);

        if (!hasAuthorization(request)) {
            log.info("Request of {} has not authorized", request.path());
        }

        log.info("{} tries to request for first visit apply", JWTUtils.extractUserId(getJWT(request)));
    }

    @Before("deleteApplyById()")
    public void beforeDeleteApplyById(JoinPoint joinPoint) {
        CommonUtils.printlnBefore(log, "Delete Apply");
    }

    @After("deleteApplyById()")
    public void afterDeleteApplyById(JoinPoint joinPoint) {
        CommonUtils.printlnAfter(log, "Delete Apply");
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
