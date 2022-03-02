package cn.tzq0301.gateway;

import cn.tzq0301.gateway.message.SmsUtils;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author tzq0301
 * @version 1.0
 */
@SpringBootTest
public class SmsTest {
    @Autowired
    SmsUtils smsUtils;

    @Test
    @Disabled
    void test() throws TencentCloudSDKException {
        smsUtils.sendSmsToChinesePhones(new String[]{"18968909990"}, new String[]{SmsUtils.generateRandomCode(6)});
    }

    @Test
    @Disabled
    void sendToDj() throws TencentCloudSDKException {
        assertNotNull(smsUtils.sendSmsToChinesePhones(new String[]{"18227690705"}, new String[]{"爱你哟宝贝"}));
    }
}
