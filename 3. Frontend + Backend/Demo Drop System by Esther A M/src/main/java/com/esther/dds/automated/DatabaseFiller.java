package com.esther.dds.automated;

import com.esther.dds.domain.*;
import com.esther.dds.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

//This class only serves to fill initial data in the database

@Component
public class DatabaseFiller implements CommandLineRunner {

    private UserRepository userRepository;
    private BoUserRepository boUserRepository;
    private RoleRepository roleRepository;
    private BoRoleRepository boRoleRepository;

    public DatabaseFiller(UserRepository userRepository, BoUserRepository boUserRepository, RoleRepository roleRepository, BoRoleRepository boRoleRepository) {
        this.userRepository = userRepository;
        this.boUserRepository = boUserRepository;
        this.roleRepository = roleRepository;
        this.boRoleRepository = boRoleRepository;
    }

    //individual entries State
    public State state1 = new State("Pending", "The Admin should set a 'In-review message'");
    public State state2 = new State("Rejected", "The Admin should enter a 'Rejection message'");
    public State state3 = new State("Sent", "The Admin should enter a 'Sent message'");


    @Override
    public void run(String... args) throws Exception {
        //add users to roles
        addUsersAndRoles();
        //add users to roles
        addBoUsersAndRoles();
    }

    //individual entry User
    //Encodes the raw password
    //has been placed outside of "addUsersAndRoles" on purpose
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    String secret = "{bcrypt}" + encoder.encode("pass");
    User user1 = new User("info@garrix.com", secret, "Martijn", "Garritssen", "Martin Garrix", "I thought, You know what? You might need another Talent to recruit" , "/serverside_profileimages/martijn.jpg",true);
    User user2 = new User("info@user2.com", secret, "Martine", "Dijkraam", "DJ Martine", "Love makin music, Love gettin inspired by nature, Mexican food are the best" , "/serverside_profileimages/martine.jpg",true);


    //Refactor in future (many to one relationship to role & different classes)
    private void addUsersAndRoles() {

        Role userRole = new Role("ROLE_USER");
        roleRepository.save(userRole);

        user1.addRole(userRole);
        user2.addRole(userRole);

        user1.setConfirmPassword(secret);
        user2.setConfirmPassword(secret);

        userRepository.save(user1);
        userRepository.save(user2);


//        User admin = new User("admin.com",secret ,true);
//        admin.addRoles(new HashSet<>(Arrays.asList(userRole,adminRole)));
//        userRepository.save(admin);
    }

    private void addBoUsersAndRoles() {
        BoRole boUserRole = new BoRole("ROLE_BO-USER");
        boRoleRepository.save(boUserRole);

        BoUser bo1 = new BoUser("bo.com", secret, "Floris", "Roddelaar",true); //note that password "secret" is re-used from user
        bo1.addBoRole(boUserRole);
        bo1.setConfirmPassword(secret); //note that password "secret" is re-used from user
        boUserRepository.save(bo1);
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


