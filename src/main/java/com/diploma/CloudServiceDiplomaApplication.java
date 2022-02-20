package com.diploma;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties
@SpringBootApplication
public class CloudServiceDiplomaApplication {
    public static void main(String[] args) {
        SpringApplication.run(com.diploma.CloudServiceDiplomaApplication.class, args);
    }

}
