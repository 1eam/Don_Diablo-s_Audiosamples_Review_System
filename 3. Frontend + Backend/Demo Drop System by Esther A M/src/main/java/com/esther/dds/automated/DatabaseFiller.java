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
    @Override
    public void run(String... args) throws Exception {

    }

    //individual entries State
    public State state1 = new State("Pending", "The Admin should set a 'In-review message'");
    public State state2 = new State("Rejected", "The Admin should enter a 'Rejection message'");
    public State state3 = new State("Sent", "The Admin should enter a 'Sent message'");

    @Bean
    CommandLineRunner runner(DemoRepository demoRepository, StateNameRepository stateNameRepository) {
        return args -> {

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
            demo2.setState(state2);
            demo3.setState(state3);

            //save the demos
            demoRepository.save(demo1);
            demoRepository.save(demo2);
            demoRepository.save(demo3);

            //change state message
            state1.setMessage("gg");


        };
    }

}


