package com.esther.dds;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication

public class DdsApplication {

    private static final Logger log = LoggerFactory.getLogger(DdsApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(DdsApplication.class, args);
    }


}
