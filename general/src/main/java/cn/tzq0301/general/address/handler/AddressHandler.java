package cn.tzq0301.general.address.handler;

import cn.tzq0301.entity.Records;
import cn.tzq0301.general.address.service.AddressService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

/**
 * @author tzq0301
 * @version 1.0
 */
@Component
@AllArgsConstructor
public class AddressHandler {
    private final AddressService addressService;

    public Mono<ServerResponse> listAddresses(ServerRequest request) {
        return addressService.listAddress()
                .collect(Collectors.toList())
                .map(Records::new)
                .flatMap(ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)::bodyValue);
    }
}
