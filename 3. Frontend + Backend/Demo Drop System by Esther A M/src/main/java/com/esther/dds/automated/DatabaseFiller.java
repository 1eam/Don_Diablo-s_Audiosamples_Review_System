package com.esther.dds.automated;

import com.esther.dds.domain.Demo;
import com.esther.dds.domain.Role;
import com.esther.dds.domain.State;
import com.esther.dds.domain.User;
import com.esther.dds.repositories.DemoRepository;
import com.esther.dds.repositories.RoleRepository;
import com.esther.dds.repositories.StateNameRepository;
import com.esther.dds.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;

//This class only serves to fill initial data in the database

@Component
public class DatabaseFiller implements CommandLineRunner {

    private UserRepository userRepository;
    private RoleRepository roleRepository;

    public DatabaseFiller(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    //individual entries State
    public State state1 = new State("Pending", "The Admin should set a 'In-review message'");
    public State state2 = new State("Rejected", "The Admin should enter a 'Rejection message'");
    public State state3 = new State("Sent", "The Admin should enter a 'Sent message'");


    @Override
    public void run(String... args) throws Exception {

        //add users to roles
        addUsersAndRoles();

    }

    //individual entry User
    //Encodes the raw password
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    String secret = "{bcrypt}" + encoder.encode("pass");
    User user1 = new User("user.com", secret, "Martijn", "Garritssen", "Martin Garrix", "I thought, You know what? You might need another Talent to recruit" , "/serverside_profileimages/martijn.jpg",true);
    User user2 = new User("user2.com", secret, "Martine", "Dijkraam", "DJ Martine", "Love makin music, Love gettin inspired by nature, Mexican food are the best" , "/serverside_profileimages/martine.jpg",true);


    //Refactor in future (many to one relationship to role & different classes)
    private void addUsersAndRoles() {

        Role userRole = new Role("ROLE_USER");
        roleRepository.save(userRole);
//        Role adminRole = new Role("ROLE_ADMIN");
//        roleRepository.save(adminRole);

        user1.addRole(userRole);
        user2.addRole(userRole);

        user1.setConfirmPassword(secret);
        user2.setConfirmPassword(secret);

        userRepository.save(user1);
        userRepository.save(user2);

//        BoUser bo1 = new BoUser("bo.com", secret, "Floris Roddelaar",true);
//        bo1.addRole(adminRole);
//        bo1.setConfirmPassword(secret);
//        userRepository.save(bo1);
//
//        User admin = new User("admin.com",secret ,true);
//        admin.addRoles(new HashSet<>(Arrays.asList(userRole,adminRole)));
//        userRepository.save(admin);
    }


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
            Demo demo3 = new Demo("Adventure", " I used 13 different vst's, to much to name in this description. I also mastered the track with some help of a friend, who also found me a vocalist. Great Right!?", "/serverside_audiofiles/house deep 2.mp3");

            //save the demos
            demoRepository.save(demo1);
            demoRepository.save(demo2);
            demoRepository.save(demo3);

            //assign demo to state
            demo1.setState(state1);
            demo2.setState(state2);
            demo3.setState(state3);

            //assign user to demo;
            demo1.setUser(user1);
            demo2.setUser(user1);
            demo3.setUser(user1);

            //save the demos again
            demoRepository.save(demo1);
            demoRepository.save(demo2);
            demoRepository.save(demo3);

            //change state message
            state1.setMessage("gg");


        };

    }

}


