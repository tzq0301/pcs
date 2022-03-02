package cn.tzq0301.gateway.login.manager;

import cn.tzq0301.gateway.message.SmsUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author tzq0301
 * @version 1.0
 */
@Service
@AllArgsConstructor
public class LoginManager {
    private final SmsUtils smsUtils;
}
