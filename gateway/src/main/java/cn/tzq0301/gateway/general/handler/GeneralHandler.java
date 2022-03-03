package cn.tzq0301.gateway.general.handler;

import cn.tzq0301.gateway.general.entity.PhoneResponseCodeEnum;
import cn.tzq0301.gateway.general.service.GeneralService;
import cn.tzq0301.gateway.util.PhoneUtils;
import cn.tzq0301.result.Result;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static cn.tzq0301.gateway.general.entity.PhoneResponseCodeEnum.PHONE_FORMAT_ERROR;
import static cn.tzq0301.gateway.general.entity.PhoneResponseCodeEnum.PHONE_NOT_FOUNT;

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
        String phone = request.pathVariable("phone");

        if (!PhoneUtils.isValid(phone)) {
            log.info("Phone {} is in invalid format", phone);
            return ServerResponse.ok().bodyValue(Result.error(PHONE_FORMAT_ERROR));
        }

        if (!generalService.isPhoneInEnduranceContainer(phone)) {
            log.info("Phone {} is not in the endurance container", phone);
            return ServerResponse.ok().bodyValue(Result.error(PHONE_NOT_FOUNT));
        }

        return ServerResponse.ok().build();
    }
}
