package cn.tzq0301.message.mq.validation;

import cn.tzq0301.message.message.SmsUtils;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;

import static cn.tzq0301.message.config.RedisConfig.SMS_NAMESPACE_PREFIX;
import static cn.tzq0301.message.message.SmsUtils.EXPIRATION;

/**
 * @author tzq0301
 * @version 1.0
 */
@Component
@RabbitListener(queues = "pcs-message-validation-code")
@AllArgsConstructor
@Log4j2
public class ValidationCodeListener {
    private final ReactiveRedisTemplate<String, Object> redisTemplate;

    private final SmsUtils smsUtils;

    private static final int DIGITS_NUMBER = 6;

    @RabbitHandler
    public void sendMessageContainsValidationCode(String phone) {
        String validationCode = SmsUtils.generateRandomCode(DIGITS_NUMBER);

        try {
            smsUtils.sendSmsToChinesePhones(new String[]{phone}, new String[]{validationCode});
        } catch (TencentCloudSDKException e) {
            log.error(e.getMessage());
            return;
        }

        String key = SMS_NAMESPACE_PREFIX + phone;
        redisTemplate.opsForValue().set(key, validationCode)
                .filter(Boolean::booleanValue)
                .flatMap(value -> redisTemplate.expire(key, EXPIRATION))
                .subscribe();
    }
}
