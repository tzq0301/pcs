package cn.tzq0301.gateway.general.service;

import cn.tzq0301.gateway.security.PcsUserManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author tzq0301
 * @version 1.0
 */
@Service
@AllArgsConstructor
public class GeneralService {
    private final PcsUserManager userManager;

    public boolean isPhoneInEnduranceContainer(final String phone) {
        return userManager.isPhoneInEnduranceContainer(phone);
    }
}
