package com.esther.dds.service;

import com.esther.dds.domain.Demo;
import com.esther.dds.domain.User;
import com.esther.dds.repositories.DemoRepository;
import com.esther.dds.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final RoleService roleService;
    private final MailService mailService;
    private DemoRepository demoRepository;

    public UserService(UserRepository userRepository, RoleService roleService, MailService mailService, DemoRepository demoRepository) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.mailService = mailService;
        this.demoRepository = demoRepository;
        //make new encoder everytime
        encoder = new BCryptPasswordEncoder();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public User register(User user) {
        // disable the user
        user.setEnabled(false);

        // take the password from the form and encode
        String secret = "{bcrypt}" + encoder.encode(user.getPassword());
        user.setPassword(secret);

        // confirm the password
        user.setConfirmPassword(secret);

        // assign a role to this user
        user.addRole(roleService.findByName("ROLE_USER"));

        // set an activation code
        user.setActivationCode(UUID.randomUUID().toString());

        // save user
        save(user);

        // send the activation email
        sendActivationEmail(user);

        // return the user
        return user;
    }

    public void sendActivationEmail(User user) {
        mailService.sendActivationEmail(user);
    }

    public void sendWelcomeEmail(User user) {
        mailService.sendWelcomeEmail(user);
    }

    public Optional<User> findByEmailAndActivationCode(String email, String activationCode) {
        return userRepository.findByEmailAndActivationCode(email,activationCode);
    }

    public User update(User user) {
        //to stop confirmPassword from complaining
        user.setPassword(user.getPassword());
        user.setConfirmPassword(user.getPassword());

        return userRepository.save(user);
    }


    public User editPassword(User user, String oldPassword, String password) {
        String currentPassword = user.getPassword();
        //this removes the "{bcrypt}" prefix. This has to be done first. in order for BCrypts .matches method to work
        String currentPwWithoutPrefix = currentPassword.substring(8);

        if( encoder.matches(oldPassword, currentPwWithoutPrefix)){
            //Edit the new password
            String secret = "{bcrypt}" + encoder.encode(password);
            user.setPassword(secret);

            // setconfirm password field
            user.setConfirmPassword(secret);
            userRepository.save(user);

        } else {
            logger.error("incorrect password, try again: ");
            //print bcrypted validation password
            //logger.error(currentPwWithoutPrefix);
        }

        return user;
    }

    public void delete(User user) {
        List<Demo> demosByUser = demoRepository.findByUserId(user.getId());
        demoRepository.deleteAll(demosByUser);
        userRepository.delete(user);

    }
}