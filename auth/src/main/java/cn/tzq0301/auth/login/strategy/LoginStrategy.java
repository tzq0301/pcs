package cn.tzq0301.auth.login.strategy;

import cn.tzq0301.auth.entity.user.User;
import reactor.core.publisher.Mono;

/**
 * 登录方式的策略接口（策略模式）
 *
 * @author tzq0301
 * @version 1.0
 */
public interface LoginStrategy {
    Mono<User> login(String principal, String certification);
}
