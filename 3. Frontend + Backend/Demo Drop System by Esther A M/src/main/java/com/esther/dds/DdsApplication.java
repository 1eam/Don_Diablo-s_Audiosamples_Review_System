package com.esther.dds;

import com.esther.dds.domain.Demo;
import com.esther.dds.domain.State;
import com.esther.dds.repositories.DemoRepository;
import com.esther.dds.repositories.StateNameRepository;
import org.ocpsoft.prettytime.PrettyTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
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

    @Bean
    CommandLineRunner runner(DemoRepository demoRepository, StateNameRepository stateNameRepository) {
        return args -> {

            //individual entries State
            State state1 = new State("Pending", "The Admin should enter a 'Pending message'");
            State state2 = new State("Rejected", "The Admin should enter a 'Rejection message'");
            State state3 = new State("Sent", "The Admin should enter a 'Sent message'");

            //save state
            stateNameRepository.save(state1);
            stateNameRepository.save(state2);
            stateNameRepository.save(state3);

            //individual entries Demo
            Demo demo1 = new Demo("Deep House 4", "I didnt wanna put a description. Sorry", "/serverside_audiofiles/deep house 4.mp3");
            Demo demo2 = new Demo("Virus", "You Gotta love it, it was a hit song", "/serverside_audiofiles/virus.mp3");
            Demo demo3 = new Demo("Birus", " Duplicate You Gotta love it, it was a hit song", "/serverside_audiofiles/virus.mp3");

            //save the demos
            demoRepository.save(demo1);
            demoRepository.save(demo2);
            demoRepository.save(demo3);

            //assign demo to state
            demo1.setState(state1);
            demo2.setState(state1);
            demo3.setState(state3);

            //save the demos
            demoRepository.save(demo1);
            demoRepository.save(demo2);
            demoRepository.save(demo3);

        };
    }

}
