package cn.tzq0301.util;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author tzq0301
 * @version 1.0
 */
public final class PageUtils {
    public static <T> Mono<List<T>> pagingFlux(Flux<T> flux, int offset, int limit) {
        return flux.collectList()
                .map(list -> list.subList(
                        Math.min(offset * limit, list.size()),
                        Math.min((offset + 1) * limit, list.size())));
    }

    public static <T, U> Mono<List<U>> pagingFlux(Flux<T> flux, int offset, int limit, Function<T, U> function) {
        return flux.collectList()
                .map(list -> list.subList(
                        Math.min(offset * limit, list.size()),
                        Math.min((offset + 1) * limit, list.size())))
                .map(list -> list.stream().map(function).collect(Collectors.toList()));
    }

    private PageUtils() {}
}
