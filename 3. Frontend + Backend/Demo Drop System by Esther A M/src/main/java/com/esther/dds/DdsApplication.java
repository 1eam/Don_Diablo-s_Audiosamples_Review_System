package com.esther.dds;

import org.ocpsoft.prettytime.PrettyTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DdsApplication {

    private static final Logger log = LoggerFactory.getLogger(DdsApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(DdsApplication.class, args);
    }

//    @Bean
////    PrettyTime prettyTime(){
////        return new PrettyTime();
////    }
}
