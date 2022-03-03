package cn.tzq0301.message.mq;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author tzq0301
 * @version 1.0
 */
@Component
@RabbitListener(queues = "pcs-message-validation-code")
public class ValidationCodeListener {
    @RabbitHandler
    public void sendMessageContainsValidationCode(String message) {

    }
}
