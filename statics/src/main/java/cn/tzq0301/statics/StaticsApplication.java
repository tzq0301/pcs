package cn.tzq0301.statics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author tzq0301
 * @version 1.0
 */
@SpringBootApplication
@EnableDiscoveryClient
public class StaticsApplication {

    public static void main(String[] args) {
        SpringApplication.run(StaticsApplication.class, args);
    }

}
