package com.esther.dds.service;

import com.esther.dds.domain.User;
import com.esther.dds.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserService {

    private final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final RoleService roleService;

    public UserService(UserRepository userRepository, RoleService roleService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        //make new encoder everytime
        encoder = new BCryptPasswordEncoder();
    }

    public User register(User user) {
        // take the password from the form and encode
        String secret = "{bcrypt}" + encoder.encode(user.getPassword());
        user.setPassword(secret);
        // confirm password

        // assign a role to this user
        user.addRole(roleService.findByName("ROLE_USER"));
        // set an activation code

        // disable the user
        user.setEnabled(false);
        // save user
        save(user);

        // send the activation email
        sendActivationEmail(user);

        // return the user
        return user;
    }


    public void sendActivationEmail(User user) {
        // ... do something
    }

    public User save(User user) {
        return userRepository.save(user);
    }
}