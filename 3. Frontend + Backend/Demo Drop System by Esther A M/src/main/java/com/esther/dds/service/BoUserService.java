package com.esther.dds.service;

import com.esther.dds.domain.BoUser;
import com.esther.dds.domain.Demo;
import com.esther.dds.repositories.BoUserRepository;
import com.esther.dds.repositories.DemoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BoUserService {

    private final Logger logger = LoggerFactory.getLogger(BoUserService.class);
    private final BoUserRepository boUserRepository;
    private final BCryptPasswordEncoder encoder;
    private final BoRoleService boRoleService;
    private final MailService mailService;
    private DemoRepository demoRepository;

    public BoUserService(BoUserRepository boUserRepository, BoRoleService boRoleService, MailService mailService, DemoRepository demoRepository) {
        this.boUserRepository = boUserRepository;
        this.boRoleService = boRoleService;
        this.mailService = mailService;
        this.demoRepository = demoRepository;
        //make new encoder every time
        encoder = new BCryptPasswordEncoder();
    }

    public Optional<BoUser> findById(Long id) {
        return boUserRepository.findById(id);
    }

    public Optional<BoUser> findByEmail(String email){
        return boUserRepository.findByEmail(email);
    }

    public List<BoUser> findAll() {
        return boUserRepository.findAll();
    }

    public BoUser save(BoUser boUser) {
        return boUserRepository.save(boUser);
    }

    public BoUser register(BoUser boUser) {
        // take the password from the form and encode
        String secret = "{bcrypt}" + encoder.encode(boUser.getPassword());
        boUser.setPassword(secret);

        // confirm password
        boUser.setConfirmPassword(secret);

//        boUser.setOldPassword(secret);

        // assign a role to this bo-User
        boUser.addBoRole(boRoleService.findByName("ROLE_BO-USER")); //check

        // set an activation code
        boUser.setActivationCode(UUID.randomUUID().toString());

        // disable the bo-User
        boUser.setEnabled(false);
        // save bo-User
        save(boUser);

        // send the activation email
        sendActivationEmail(boUser);

        // return the bo-User
        return boUser;
    }

    public void sendActivationEmail(BoUser boUser) {
        mailService.sendBoActivationEmail(boUser);
    }

//    future feature
//    public void sendWelcomeEmail(BoUser boUser) {mailService.sendBoWelcomeEmail(boUser);}

    public Optional<BoUser> findByEmailAndActivationCode(String email, String activationCode) {
        return boUserRepository.findByEmailAndActivationCode(email,activationCode);
    }

    public BoUser update(BoUser boUser) {
        //to stop confirmPassword from complaining
        boUser.setPassword(boUser.getPassword());
        boUser.setConfirmPassword(boUser.getPassword());

        return boUserRepository.save(boUser);
    }


    public BoUser editPassword(BoUser boUser, String oldPassword, String password) { //Todo: checl oldpassword refactor
        String currentPassword = boUser.getPassword();
        //this removes the "{bcrypt}" prefix. This has to be done first. in order for BCrypts .matches method to work
        String currentPwWithoutPrefix = currentPassword.substring(8);

        if( encoder.matches(oldPassword, currentPwWithoutPrefix)){
            //Edit the new password
            String secret = "{bcrypt}" + encoder.encode(password);
            boUser.setPassword(secret);

            // setconfirm password field
            boUser.setConfirmPassword(secret);
            boUserRepository.save(boUser);

        } else {
            logger.error("incorrect password, try again: ");
            //print bcrypted validation password
            //logger.error(currentPwWithoutPrefix);
        }

        return boUser;
    }

    public void delete(BoUser boUser) {
//        List<Demo> demosByUser = demoRepository.findByReviewerId(boUser.getId());
//        instead set all to "deleted bo-user"
//        demoRepository.deleteAll(demosByUser);
        boUserRepository.delete(boUser);
    }
}