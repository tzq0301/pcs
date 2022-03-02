package cn.tzq0301.message;

import cn.tzq0301.message.message.SmsUtils;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

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
    void sendToTzq() throws TencentCloudSDKException {
        assertNotNull(smsUtils.sendSmsToChinesePhones(new String[]{"18968909990"}, new String[]{"123456"}));
    }

    @Test
    @Disabled
    void sendToDj() throws TencentCloudSDKException {
        assertNotNull(smsUtils.sendSmsToChinesePhones(new String[]{"18227690705"}, new String[]{"010122"}));
    }

    @Test
    @Disabled
    void sendToWx() throws TencentCloudSDKException {
        assertNotNull(smsUtils.sendSmsToChinesePhones(new String[]{"18198646220"}, new String[]{"123456"}));
    }
}
