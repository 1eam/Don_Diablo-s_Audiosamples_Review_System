package com.esther.dds;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication

public class DdsApplication {

    private static final Logger log = LoggerFactory.getLogger(DdsApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(DdsApplication.class, args);
    }

    //This bean will not be needed until Thymeleaf startr includes it.
//    @Bean
//    public SpringSecurityDialect securityDialect() {
//        return new SpringSecurityDialect();
//    }
}
