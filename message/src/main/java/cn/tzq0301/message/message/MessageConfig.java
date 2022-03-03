package cn.tzq0301.message.message;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;

/**
 * @author tzq0301
 * @version 1.0
 */
@SpringBootConfiguration
@RefreshScope
public class MessageConfig {
    @Value("${sms.secretId}")
    private String secretId;

    @Value("${sms.secretKey}")
    private String secretKey;

    @Value("${sms.region}")
    private String region;

    @Bean
    public SmsClient smsClient() {
        Credential credential = new Credential(secretId, secretKey);

        ClientProfile clientProfile = new ClientProfile();

        return new SmsClient(credential, region, clientProfile);
    }
}
