package cn.tzq0301.consult;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ConsultApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsultApplication.class, args);
    }

}
