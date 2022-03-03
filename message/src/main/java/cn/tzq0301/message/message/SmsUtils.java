package cn.tzq0301.message.message;

import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author tzq0301
 * @version 1.0
 */
@Component
@RefreshScope
@Log4j2
public class SmsUtils {
    public static final String CHINESE_PHONE_PREFIX = "+86";

    @Value("${sms.sdkAppId}")
    private String sdkAppId;

    @Value("${sms.signName}")
    private String signName;

    @Value("${sms.templateId}")
    private String templateId;

    private final SmsClient smsClient;

    public SmsUtils(SmsClient smsClient) {
        this.smsClient = smsClient;
    }

    /**
     * 发送短信
     *
     * @param phoneNumberSet   手机号码：采用 E.164 标准，+[国家或地区码][手机号]
     *                         （示例如：+8613711112222， 其中前面有一个+号 ，86为国家码，
     *                         13711112222为手机号，最多不要超过200个手机号）
     * @param templateParamSet 模板参数: 若无模板参数，则设置为空
     * @return {@link SendSmsResponse}
     * @throws TencentCloudSDKException 短信发送服务异常
     */
    public SendSmsResponse sendSmsToChinesePhones(String[] phoneNumberSet, String[] templateParamSet) throws TencentCloudSDKException {
        SendSmsRequest request = new SendSmsRequest();

        request.setSmsSdkAppId(sdkAppId);
        request.setSignName(signName);
        request.setSenderId("");
        request.setTemplateId(templateId);

        phoneNumberSet = Arrays.stream(phoneNumberSet)
                .map(phone -> phone.startsWith(SmsUtils.CHINESE_PHONE_PREFIX)
                        ? phone
                        : SmsUtils.CHINESE_PHONE_PREFIX + phone)
                .toArray(String[]::new);
        request.setPhoneNumberSet(phoneNumberSet);

        request.setTemplateParamSet(templateParamSet);

        SendSmsResponse response;
        try {
            response = smsClient.SendSms(request);
        } catch (TencentCloudSDKException exception) {
            log.error("Can not send message with following template parameter set to {}: {}", phoneNumberSet, templateParamSet);
            throw exception;
        }

        return response;
    }

    /**
     * 生成共 digitsNumber 位的字符串验证码
     *
     * 例如：digitsNumber = 6 时，将生成一个 100000 ~ 999999 的验证码
     *
     * @param digitsNumber 要生成的结果的长度
     * @return 共 digitsNumber 位的字符串验证码
     */
    public static String generateRandomCode(int digitsNumber) {
        if (digitsNumber <= 0) {
            throw new IllegalArgumentException("digitsNumber can not equal or less than 0: " + digitsNumber);
        }

        ThreadLocalRandom random = ThreadLocalRandom.current();

        int baseNumber = (int) Math.pow(10, digitsNumber - 1);
        int result = random.nextInt(0, baseNumber * 9);
        result += baseNumber;

        return String.valueOf(result);
    }
}
