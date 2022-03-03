package cn.tzq0301.gateway.general.service;

import cn.tzq0301.gateway.security.PcsUserManager;
import lombok.AllArgsConstructor;
import org.springframework.amqp.AmqpException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * @author tzq0301
 * @version 1.0
 */
@Service
@AllArgsConstructor
public class GeneralService {
    private final PcsUserManager userManager;

    public Mono<Boolean> isPhoneInEnduranceContainer(final String phone) {
        return userManager.isPhoneInEnduranceContainer(phone);
    }

    public void sendValidationCode(final String phone) throws AmqpException {
        userManager.sendValidationCode(phone);
    }
}
