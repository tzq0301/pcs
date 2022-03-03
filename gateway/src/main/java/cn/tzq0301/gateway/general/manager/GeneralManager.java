package cn.tzq0301.gateway.general.manager;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

/**
 * @author tzq0301
 * @version 1.0
 */
@Service
@AllArgsConstructor
@Slf4j
public class GeneralManager {
    private final AmqpTemplate amqpTemplate;

    public void sendValidationCode(final String phone) throws AmqpException {
        amqpTemplate.convertAndSend(phone);
    }
}
