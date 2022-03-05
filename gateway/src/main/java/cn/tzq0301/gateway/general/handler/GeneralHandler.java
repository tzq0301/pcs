package cn.tzq0301.gateway.general.handler;

import cn.tzq0301.gateway.general.service.GeneralService;
import cn.tzq0301.util.PhoneUtils;
import cn.tzq0301.result.Result;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.AmqpException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static cn.tzq0301.gateway.general.entity.PhoneResponseCodeEnum.*;

/**
 * @author tzq0301
 * @version 1.0
 */
@Component
@Log4j2
@AllArgsConstructor
public class GeneralHandler {
    private final GeneralService generalService;

    public Mono<ServerResponse> sendCodeToPhone(ServerRequest request) {
        return Mono.just(request.pathVariable("phone"))
                .doOnNext(phone -> log.info("{} try to request for the validation code", phone))
                .flatMap(phone -> {
                    if (!isPhoneValid(phone)) {
                        log.info("{} is not in correct format", phone);
                        return Mono.just(Result.error(PHONE_FORMAT_ERROR));
                    }

                    log.info("{} is in correct format", phone);

                    return Mono.defer(() -> generalService.isPhoneInEnduranceContainer(phone))
                            .flatMap(phoneInEnduranceContainer -> {
                                if (!phoneInEnduranceContainer) {
                                    log.info("Phone {} is not in the endurance container", phone);
                                    return Mono.just(Result.error(PHONE_NOT_FOUNT));
                                } else {
                                    log.info("Phone {} is in the endurance container", phone);
                                    return Mono.fromRunnable(() -> generalService.sendValidationCode(phone))
                                            .thenReturn(Result.success(SUCCESS))
                                            .onErrorReturn(AmqpException.class, Result.error(MESSAGE_SEND_ERROR));
                                }
                            });
                })
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    private static boolean isPhoneValid(final String phone) {
        boolean valid = PhoneUtils.isValid(phone);

        if (!valid) {
            log.info("Phone {} is in invalid format", phone);
        }

        return valid;
    }
}
