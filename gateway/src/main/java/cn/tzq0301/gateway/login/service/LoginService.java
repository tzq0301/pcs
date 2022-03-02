package cn.tzq0301.gateway.login.service;

import cn.tzq0301.gateway.login.manager.LoginManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author tzq0301
 * @version 1.0
 */
@Component
@AllArgsConstructor
public class LoginService {
    private final LoginManager loginManager;


}
