package cn.tzq0301.message.mq.validation;

import cn.tzq0301.message.message.SmsUtils;
import lombok.AllArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static cn.tzq0301.message.message.SmsUtils.EXPIRATION;

/**
 * @author tzq0301
 * @version 1.0
 */
@Aspect
@Component
@AllArgsConstructor
public class ValidationCodeListenerAOP {
    private static final Logger log = LoggerFactory.getLogger(ValidationCodeListener.class);

    @Pointcut("execution(public void cn.tzq0301.message.mq.validation.ValidationCodeListener.sendMessageContainsValidationCode(String))")
    public void sendMessageContainsValidationCode() {
    }

    @Before(value = "sendMessageContainsValidationCode()")
    public void beforeSendMessageContainsValidationCode(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();

        if (!(args[0] instanceof String)) {
            throw new IllegalArgumentException();
        }
        String phone = (String) args[0];

        log.info("Receive validation code request from {}", phone);
    }

    @After(value = "sendMessageContainsValidationCode()")
    public void afterSendMessageContainsValidationCode(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();

        if (!(args[0] instanceof String)) {
            throw new IllegalArgumentException();
        }
        String phone = (String) args[0];

        log.info("Set expiration of {} minutes for {}", EXPIRATION.toMinutes(), phone);
    }
}
