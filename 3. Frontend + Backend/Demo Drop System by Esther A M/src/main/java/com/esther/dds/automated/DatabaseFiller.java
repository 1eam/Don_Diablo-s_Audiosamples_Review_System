package com.esther.dds.automated;

import com.esther.dds.domain.Demo;
import com.esther.dds.domain.State;
import com.esther.dds.repositories.DemoRepository;
import com.esther.dds.repositories.StateNameRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class DatabaseFiller implements CommandLineRunner {

    @Bean
    CommandLineRunner runner2(StateNameRepository stateNameRepository, DemoRepository demoRepository) {
        return args -> {

//          Demo
            demoRepository.save(new Demo("Deep Houde 4: Sana", "I didnt wanna put a description. Sorry", "/serverside_audiofiles/deep house 4.mp3"));
            demoRepository.save(new Demo("Virus", "You Gotta love it, it was a hit song", "/serverside_audiofiles/virus.mp3"));
            demoRepository.save(new Demo("Deep House 2", "Lorem ipsum I used Nexus for base, and Sylenth1 for the melody...", "/serverside_audiofiles/Deep House 2.mp3"));


//          State
            stateNameRepository.save(new State("Pending", "Admin should enter a: Pending message"));
            stateNameRepository.save(new State("Rejected", "Admin should enter a: Rejection message"));
            stateNameRepository.save(new State("Sent", "Admin should enter a: Sent message"));


        };
    }



    @Override
    public void run(String... args) throws Exception {

    }



    //    public DatabaseFiller(DemoRepository demoRepository) {
//        this.demoRepository = demoRepository;
//    }
//
//    @Override
//    public void run(String... args) {
//        Map<String,String,> demos = new HashMap<>();
//        demos.put("Dance","I Hope youll love it", "fffff");
//        demos.put("Easy way to detect Device in Java Web Application using Spring Mobile - Source code to download from GitHub","https://www.opencodez.com/java/device-detection-using-spring-mobile.htm");
//        demos.put("Tutorial series about building microservices with SpringBoot (with Netflix OSS)","https://medium.com/@marcus.eisele/implementing-a-microservice-architecture-with-spring-boot-intro-cdb6ad16806c");
//
//        demos.forEach((k,v) -> {
//            demoRepository.save(new Demo());
//            // we will do something with comments later (state)
//        });
//
//        long demoCount = demoRepository.count();
//        System.out.println("Number of links in the database: " + demoCount ); //Voor BO
//    }
}

