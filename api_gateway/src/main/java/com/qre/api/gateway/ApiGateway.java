package com.qre.api.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.qre")
public class ApiGateway {

    private Logger logger = LoggerFactory.getLogger(ApiGateway.class);

    public static void main(String[] args) {
        SpringApplication.run(ApiGateway.class, args);
    }

}
