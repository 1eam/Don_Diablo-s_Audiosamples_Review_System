package com.esther.dds.automated;

import com.esther.dds.domain.*;
import com.esther.dds.repositories.*;
import com.esther.dds.service.AdminService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

//This class serves to fill initial data to the database

@Component
public class DatabaseFiller implements CommandLineRunner {

    private UserRepository userRepository;
    private BoUserRepository boUserRepository;
    private AdminService adminService;
    private RoleRepository roleRepository;
    private BoRoleRepository boRoleRepository;
    private AdminRoleRepository adminRoleRepository;

    public DatabaseFiller(UserRepository userRepository, BoUserRepository boUserRepository, AdminService adminService, RoleRepository roleRepository, BoRoleRepository boRoleRepository, AdminRoleRepository adminRoleRepository) {
        this.userRepository = userRepository;
        this.boUserRepository = boUserRepository;
        this.adminService = adminService;
        this.roleRepository = roleRepository;
        this.boRoleRepository = boRoleRepository;
        this.adminRoleRepository = adminRoleRepository;
    }





    //The (encoded) password all generated users get.
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    String secret = "{bcrypt}" + encoder.encode("pass");


    //initialization of hashmaps that will contain all db entries in its variable.
    Map<String, Demo> demos = new HashMap<String, Demo>();
    Map<String, State> states = new HashMap<String, State>();
    Map<String, User> users = new HashMap<String, User>();
    Map<String, BoUser> boUsers = new HashMap<String, BoUser>();
    //Admin doesnt need a hashmap because there's currently only one Admin


    @Override
    public void run(String... args) throws Exception {
        //add users to roles
        addUsersAndRoles();
        addBoUsersAndRoles();
        addAdminUsersAndRoles();
    }

    private void addUsersAndRoles() {
        Role userRole = new Role("ROLE_USER");
        roleRepository.save(userRole);

        users.put("user1", new User("info@garrix.com", secret, "Martijn", "Garritssen", "Martin Garrix", "I thought, You know what? You might need another Talent to recruit" , "/serverside_profileimages/martijn.jpg",true));
        users.put("user2", new User("info@user2.com", secret, "Martine", "Dijkraam", "DJ Martine", "Love makin music, Love gettin inspired by nature, Mexican food are the best" , "/serverside_profileimages/martine.jpg",true));

        //save all users
        //"v" here is actually the "new User" object, it belongs to the Map object and it stands for value. "k" stands for key.
        users.forEach((k,v) -> {
            v.addRole(userRole);
            v.setConfirmPassword(secret);
            userRepository.save(v);
        });

    }

    private void addBoUsersAndRoles() {
        BoRole boUserRole = new BoRole("ROLE_BO-USER");
        boRoleRepository.save(boUserRole);

        boUsers.put("deletedbouser", new BoUser("deletedbouser.com", secret, "Deleted user", "Deleted user",true));
        boUsers.put("bo1", new BoUser("info@bo.com", secret, "Floris", "Roddelaar",true));
        boUsers.put("bo2", new BoUser("info@bo2.com", secret, "Kasper", "D. HoogZoon",true));
        boUsers.put("bo3", new BoUser("info@bo3.com", secret, "Joris", "Dobbelaar",true));
        boUsers.put("bo4", new BoUser("info@bo4.com", secret, "Daphne", "van Veldt",true));

        //save all boUsers at once
        boUsers.forEach((k,v) -> {
            v.addBoRole(boUserRole);
            v.setConfirmPassword(secret);
            boUserRepository.save(v);
        });

    }

    private void addAdminUsersAndRoles() {
        //admin user entry, no need for a hashmap (yet, unless there will be multiple admin roles in the future)
        Admin admin = new Admin("info@admin.com", secret, true);

        //assign a role to admin
        AdminRole adminRole = new AdminRole("ROLE_ADMIN");
        adminRoleRepository.save(adminRole);
        admin.addAdminRole(adminRole);

        //register admin user: sets account enabled to false, generates random password, and sends email with pw + activationcode
        adminService.register(admin);


    }

    @Bean
    CommandLineRunner runner(DemoRepository demoRepository, StateNameRepository stateNameRepository) {
        return args -> {
            //individual entries State
            states.put("state1", new State("Pending", "The Admin should set a 'In-review message'"));
            states.put("state2", new State("Rejected", "The Admin should enter a 'Rejection message'"));
            states.put("state3", new State("Sent", "The Admin should enter a 'Sent message'"));

            //save all states
            states.forEach((k,v) -> {
                stateNameRepository.save(v);
            });

            //individual entries Demo
            demos.put("demo1", new Demo("Deep House 4", "I didnt wanna put a description. Sorry", "/serverside_audiofiles/deep house 4.mp3"));
            demos.put("demo2", new Demo("Virus", "You Gotta love it, it was a hit song", "/serverside_audiofiles/virus.mp3"));
            demos.put("demo3", new Demo("Adventure", " I used 13 different vst's, to much to name in this description. I also mastered the track with some help of a friend, who also found me a vocalist. Great Right!?", "/serverside_audiofiles/house deep 2.mp3"));

            //assign demos to state
            demos.forEach((k,v) -> {
                if (k== "demo14"||k== "demo16"){
                    v.setState(states.get("state3")); //Sent state
                } else if (k== "demo6"||k== "demo9"||k== "demo13"||k== "demo19"){
                    v.setState(states.get("state2")); //Rejected state
                } else {
                    v.setState(states.get("state1")); //the rest is Pending state
                }
            });

            //assign demos to user
            //todo: randomize demo nr so same profile picture wont be shows after each other
            demos.forEach((k,v) -> {
                if (k== "demo1"||k== "demo2"||k== "demo3"){
                    v.setUser(users.get("user1"));
                } else if (k== "demo4"||k== "demo5"||k== "demo6"){
                    v.setUser(users.get("user2"));
                } else if (k== "demo7"||k== "demo8"||k== "demo9"){
                    v.setUser(users.get("user3"));
                } else if (k== "demo10"||k== "demo11"||k== "demo12"){
                    v.setUser(users.get("user4"));
                } else if (k== "demo13"||k== "demo14"||k== "demo15"){
                    v.setUser(users.get("user5"));
                } else if (k== "demo16"||k== "demo17"||k== "demo18"){
                    v.setUser(users.get("user6"));
                } else if (k== "demo19"||k== "demo20"||k== "demo21"){
                    v.setUser(users.get("user7"));
                }
            });

            //assign back-office reviewer to demo:
            //rejected: "demo6"||k== "demo9"||k== "demo13"||k== "demo19"
            demos.get("demo6").setReviewedBy(boUsers.get("bo2"));
            demos.get("demo9").setReviewedBy(boUsers.get("bo3"));
            demos.get("demo13").setReviewedBy(boUsers.get("bo2"));
            demos.get("demo19").setReviewedBy(boUsers.get("bo4"));
            //sent: "demo14"||k== "demo16"
            demos.get("demo14").setReviewedBy(boUsers.get("bo2"));
            demos.get("demo16").setReviewedBy(boUsers.get("bo3"));


            //save all demos.
            demos.forEach((k,v) -> {
                demoRepository.save(v);
            });

        };

    }

}


