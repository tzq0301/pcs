package cn.tzq0301.gateway.login.service;

import cn.tzq0301.gateway.login.manager.LoginManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author tzq0301
 * @version 1.0
 */
@Service
@AllArgsConstructor
public class LoginService {
    private final LoginManager loginManager;
}
