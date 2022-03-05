package cn.tzq0301.visit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class VisitApplication {

    public static void main(String[] args) {
        SpringApplication.run(VisitApplication.class, args);
    }

}
