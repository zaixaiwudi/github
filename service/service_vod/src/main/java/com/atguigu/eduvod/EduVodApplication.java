package com.atguigu.eduvod;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude= DataSourceAutoConfiguration.class)
@ComponentScan({"com.atguigu"})
@EnableDiscoveryClient
public class EduVodApplication {
    public static void main(String[] args) {
        SpringApplication.run(EduVodApplication.class, args);
    }
}
