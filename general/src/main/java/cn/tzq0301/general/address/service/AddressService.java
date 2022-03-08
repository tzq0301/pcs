package cn.tzq0301.general.address.service;

import cn.tzq0301.general.address.manager.AddressManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author tzq0301
 * @version 1.0
 */
@Service
@AllArgsConstructor
public class AddressService {
    private final AddressManager addressManager;

    public Flux<String> listAddress() {
        return addressManager.listAddress();
    }

//    private Object listAvailableAddressesByDay(String day, Integer from) {
//        return addressManager.listAddress()
//                .collect(Collectors.toSet())
//
//
//    }
}
