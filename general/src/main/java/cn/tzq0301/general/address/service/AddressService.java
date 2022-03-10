package cn.tzq0301.general.address.service;

import cn.tzq0301.general.address.manager.AddressManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

/**
 * @author tzq0301
 * @version 1.0
 */
@Service
@AllArgsConstructor
public class AddressService {
    private final AddressManager addressManager;

    public Mono<List<String>> listAddress() {
        return addressManager.listAddress()
                .collectList();
    }

    public Mono<List<String>> listNonSpareAddressByWeekday(final int weekday, final int from) {
        return addressManager.listNonSpareAddressByWeekday(weekday, from);
    }

    public Mono<List<String>> listNonSpareAddressByDay(final LocalDate day, final int from) {
        return addressManager.listNonSpareAddressByDay(day, from);
    }
}
