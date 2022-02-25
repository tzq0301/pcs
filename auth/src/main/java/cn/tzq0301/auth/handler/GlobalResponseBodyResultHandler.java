package cn.tzq0301.auth.handler;

import cn.tzq0301.result.Result;
import org.springframework.core.MethodParameter;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.web.reactive.HandlerResult;
import org.springframework.web.reactive.accept.RequestedContentTypeResolver;
import org.springframework.web.reactive.result.method.annotation.ResponseBodyResultHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Function;

/**
 * 配置 WebFlux 统一返回体
 *
 * @author tzq0301
 * @version 1.0
 */
public class GlobalResponseBodyResultHandler extends ResponseBodyResultHandler {
    private static final Result<?> DEFAULT_RESULT = Result.success();

    private static final MethodParameter DEFAULT_METHOD_PARAMETER;

    static {
        try {
            DEFAULT_METHOD_PARAMETER = new MethodParameter(GlobalResponseBodyResultHandler.class.getDeclaredMethod("methodForParams"), -1);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("无法获取方法参数");
        }
    }

    public GlobalResponseBodyResultHandler(List<HttpMessageWriter<?>> writers, RequestedContentTypeResolver resolver) {
        super(writers, resolver);
    }

    public GlobalResponseBodyResultHandler(List<HttpMessageWriter<?>> writers, RequestedContentTypeResolver resolver, ReactiveAdapterRegistry registry) {
        super(writers, resolver, registry);
    }

    @Override
    public Mono<Void> handleResult(ServerWebExchange exchange, HandlerResult result) {
        Object returnValue = result.getReturnValue();

        if (returnValue instanceof Mono) {
            return writeBody(
                    ((Mono<?>) returnValue)
                            .map((Function<Object, Object>) GlobalResponseBodyResultHandler::wrapResult)
                            .defaultIfEmpty(DEFAULT_RESULT), DEFAULT_METHOD_PARAMETER, exchange);
        }

        if (returnValue instanceof Flux) {
            return writeBody(
                    ((Flux<?>) result.getReturnValue())
                            .collectList()
                            .map((Function<Object, Object>) GlobalResponseBodyResultHandler::wrapResult)
                            .defaultIfEmpty(DEFAULT_RESULT), DEFAULT_METHOD_PARAMETER, exchange);
        }

        return writeBody(wrapResult(returnValue), DEFAULT_METHOD_PARAMETER, exchange);
    }

    private static Result<?> wrapResult(Object body) {
        if (body instanceof Result) {
            return (Result<?>) body;
        }
        return Result.success(body);
    }

    private static Mono<Result<?>> methodForParams() {
        return null;
    }

}
