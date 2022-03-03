package cn.tzq0301.gateway.config;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
public class RabbitMQConfig {
    @Value("${aliyun.mq.host}")
    private String host;

    @Value("${aliyun.mq.username}")
    private String username;

    @Value("${aliyun.mq.password}")
    private String password;

    @Value("${aliyun.mq.port}")
    private Integer port;

    @Value("${aliyun.mq.virtualHost}")
    private String virtualHost;

    @Value("${aliyun.mq.queue}")
    private String queue;

    @Value("${aliyun.mq.exchange}")
    private String exchange;

    @Value("${aliyun.mq.routingKey}")
    private String routingKey;

    @Bean
    public ConnectionFactory connectionFactory() {
        com.rabbitmq.client.ConnectionFactory factory = new com.rabbitmq.client.ConnectionFactory();

        // 设置接入点，在消息队列RabbitMQ版控制台实例详情页面查看。
        factory.setHost(host);
        // 用户名，在消息队列RabbitMQ版控制台用户名密码管理页面查看。
        factory.setUsername(username);
        // 密码，在消息队列RabbitMQ版控制台用户名密码管理页面查看。
        factory.setPassword(password);
        //设置为true，开启Connection自动恢复功能；设置为false，关闭Connection自动恢复功能。
        factory.setAutomaticRecoveryEnabled(true);
        factory.setNetworkRecoveryInterval(5000);
        // 设置Vhost名称，请确保已在消息队列RabbitMQ版控制台上创建完成。
        factory.setVirtualHost(virtualHost);
        // 默认端口，非加密端口5672，加密端口5671。
        factory.setPort(port);

        return new CachingConnectionFactory(factory);
    }

    @Bean
    public AmqpTemplate amqpTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());

        rabbitTemplate.setDefaultReceiveQueue(queue);
        rabbitTemplate.setExchange(exchange);
        rabbitTemplate.setRoutingKey(routingKey);

        return rabbitTemplate;
    }
}
