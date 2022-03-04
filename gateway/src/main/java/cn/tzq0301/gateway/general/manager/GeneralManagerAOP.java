package cn.tzq0301.gateway.general.manager;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author tzq0301
 * @version 1.0
 */
@Aspect
@Component
public class GeneralManagerAOP {
    private static final Logger log = LoggerFactory.getLogger(GeneralManager.class);

    @Pointcut("execution(public void cn.tzq0301.gateway.general.manager.GeneralManager.sendValidationCode(String))")
    public void sendValidationCode() {}

    @Before(value = "sendValidationCode()")
    public void beforeSendValidationCode(JoinPoint joinPoint) {
        log.info("{} requests to send validation code", joinPoint.getArgs()[0]);
    }
}
