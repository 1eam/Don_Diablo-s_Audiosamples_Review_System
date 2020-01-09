package com.esther.dds.automated;

import com.esther.dds.domain.Demo;
import com.esther.dds.repositories.DemoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class DatabaseFiller implements CommandLineRunner {

    private DemoRepository demoRepository;

    public DatabaseFiller(DemoRepository demoRepository) {
        this.demoRepository = demoRepository;
    }

    @Override
    public void run(String... args) {
        Map<String,String> demos = new HashMap<>();
        demos.put("Securing Spring Boot APIs and SPAs with OAuth 2.0","https://auth0.com/blog/securing-spring-boot-apis-and-spas-with-oauth2/?utm_source=reddit&utm_medium=sc&utm_campaign=springboot_spa_securing");
        demos.put("Easy way to detect Device in Java Web Application using Spring Mobile - Source code to download from GitHub","https://www.opencodez.com/java/device-detection-using-spring-mobile.htm");
        demos.put("Tutorial series about building microservices with SpringBoot (with Netflix OSS)","https://medium.com/@marcus.eisele/implementing-a-microservice-architecture-with-spring-boot-intro-cdb6ad16806c");
        demos.put("Detailed steps to send encrypted email using Java / Spring Boot - Source code to download from GitHub","https://www.opencodez.com/java/send-encrypted-email-using-java.htm");
        demos.put("Build a Secure Progressive Web App With Spring Boot and React","https://dzone.com/articles/build-a-secure-progressive-web-app-with-spring-boo");
        demos.put("Building Your First Spring Boot Web Application - DZone Java","https://dzone.com/articles/building-your-first-spring-boot-web-application-ex");
        demos.put("Building Microservices with Spring Boot Fat (Uber) Jar","https://jelastic.com/blog/building-microservices-with-spring-boot-fat-uber-jar/");
        demos.put("Spring Cloud GCP 1.0 Released","https://cloud.google.com/blog/products/gcp/calling-java-developers-spring-cloud-gcp-1-0-is-now-generally-available");
        demos.put("Simplest way to Upload and Download Files in Java with Spring Boot - Code to download from Github","https://www.opencodez.com/uncategorized/file-upload-and-download-in-java-spring-boot.htm");
        demos.put("Add Social Login to Your Spring Boot 2.0 app","https://developer.okta.com/blog/2018/07/24/social-spring-boot");
        demos.put("File download example using Spring REST Controller","https://www.jeejava.com/file-download-example-using-spring-rest-controller/");

        demos.forEach((k,v) -> {
            demoRepository.save(new Demo());
            // we will do something with comments later (state)
        });

        long demoCount = demoRepository.count();
        System.out.println("Number of links in the database: " + demoCount ); //Voor BO
    }
}

