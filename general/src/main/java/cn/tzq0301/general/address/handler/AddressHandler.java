package cn.tzq0301.general.address.handler;

import cn.tzq0301.general.address.entity.Addresses;
import cn.tzq0301.general.address.service.AddressService;
import cn.tzq0301.result.Result;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author tzq0301
 * @version 1.0
 */
@Component
@AllArgsConstructor
@Log4j2
public class AddressHandler {
    private final AddressService addressService;

    public Mono<ServerResponse> listAddresses(ServerRequest request) {
        return addressService.listAddress()
                .map(Addresses::new)
                .map(Result::success)
                .flatMap(ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)::bodyValue);
    }

    public Mono<ServerResponse> listSpareAddressesByDay(ServerRequest request) {
        return null;
    }

    public Mono<ServerResponse> listSpareAddressesByWeekday(ServerRequest request) {
        return Mono.zip(
                        addressService.listAddress(),
                        addressService.listNonSpareAddressByWeekday(
                                Integer.parseInt(request.pathVariable("weekday")),
                                Integer.parseInt(request.pathVariable("from"))))
                .map(tuple -> {
                    List<String> availableAddress = tuple.getT1();
                    log.info("All addresses       -> {}", availableAddress);
                    List<String> nonSpareAddresses = tuple.getT2();
                    log.info("Non spare addresses -> {}", nonSpareAddresses);
                    availableAddress.removeAll(nonSpareAddresses);
                    log.info("All spare addresses -> {}", availableAddress);
                    return availableAddress;
                })
                .map(Addresses::new)
                .flatMap(it -> ServerResponse.ok().bodyValue(Result.success(it)));

    }
}
