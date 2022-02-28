package cn.tzq0301.auth.api;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * @author tzq0301
 * @version 1.0
 */
@Component
public class TestHandler {
    public Mono<ServerResponse> test(ServerRequest request) {
        return ServerResponse.ok().bodyValue("<h1>test</h1>");
    }

    public Mono<ServerResponse> student(ServerRequest request) {
        return ServerResponse.ok().bodyValue("<h1>student</h1>");
    }

    public Mono<ServerResponse> admin(ServerRequest request) {
        return ServerResponse.ok().bodyValue("<h1>admin</h1>");
    }
}
