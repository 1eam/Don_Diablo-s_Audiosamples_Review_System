//package com.esther.dds.automated;
//
//import com.esther.dds.domain.Demo;
//import com.esther.dds.domain.State;
//import com.esther.dds.repositories.DemoRepository;
//import com.esther.dds.repositories.StateNameRepository;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Component;
//
//@Component
//public class DatabaseFiller implements CommandLineRunner {
//    @Override
//    public void run(String... args) throws Exception {
//
//    }
//
//
//    @Bean
//    CommandLineRunner runner2(StateNameRepository stateNameRepository, DemoRepository demoRepository) {
//        return args -> {
//
//            //individual entries
//            Demo demo1 = new Demo("Deep Houde 4: Sana", "I didnt wanna put a description. Sorry", "/serverside_audiofiles/deep house 4.mp3");
//            Demo demo2 = new Demo("Virus", "You Gotta love it, it was a hit song", "/serverside_audiofiles/virus.mp3");
//
//            State state1 = new State("Pending", "Admin should enter a: Pending message");
//            State state2 = new State("Rejected", "Admin should enter a: Rejection message");
//            State state3 = new State("Sent", "Admin should enter a: Sent message");
//
//            //assign a state to demo
//            demo1.setState(state2);
//            demo2.setState(state1);
//
//            //save the demos and state
//            demoRepository.save(demo1);
//            demoRepository.save(demo2);
//
//            stateNameRepository.save(state1);
//            stateNameRepository.save(state2);
//            stateNameRepository.save(state3);
//        };
//    }
//
//
//
//
////    @Bean
////    CommandLineRunner runner2(StateNameRepository stateNameRepository, DemoRepository demoRepository) {
////        return args -> {
////
////            //Demo
////            demoRepository.save(new Demo("Deep Houde 4: Sana", "I didnt wanna put a description. Sorry", "/serverside_audiofiles/deep house 4.mp3"));
////            demoRepository.save(new Demo("Virus", "You Gotta love it, it was a hit song", "/serverside_audiofiles/virus.mp3", stateNameRepository.findById()));
////
////
////            //State
////            stateNameRepository.save(new State("Pending", "Admin should enter a: Pending message"));
////            stateNameRepository.save(new State("Rejected", "Admin should enter a: Rejection message"));
////            stateNameRepository.save(new State("Sent", "Admin should enter a: Sent message"));
////
////
////        };
////    }
//}
//
//
