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
        // disable the bo-User
        boUser.setEnabled(false);

        //generate password using 32 digit UUID, and trim beginning by 26, so the result will be a 10 digit - password
        String generatedPassword = UUID.randomUUID().toString().substring(26);
        boUser.setGeneratedPassword(generatedPassword);
        // take generated password and encode
        String secret = "{bcrypt}" + encoder.encode(generatedPassword);
        boUser.setPassword(secret);

        // confirm password
        boUser.setConfirmPassword(secret);

        // assign a role to this bo-User
        boUser.addBoRole(boRoleService.findByName("ROLE_BO-USER")); //check

        // set activation code
        boUser.setActivationCode(UUID.randomUUID().toString());

        // save bo-User
        save(boUser);

        // send activation email
        sendActivationEmail(boUser);

        // return bo-User
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
        // first lets stop confirmPassword from complaining using the following code
        boUser.setPassword(boUser.getPassword());
        boUser.setConfirmPassword(boUser.getPassword());

        //actual update function
        return boUserRepository.save(boUser);
    }


    public BoUser editPassword(BoUser boUser, String oldPassword, String password) { //Todo: check oldpassword refactor
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

        }

        return boUser;
    }

    public void delete(BoUser boUser) {
        // instead of deleting demos, set all reviewed demos to "deleted bo-user"

        List<Demo> demos = demoRepository.findByReviewedById(boUser.getId());
        for (Demo demo:demos) {
            demo.setReviewedBy(boUserRepository.findByEmail("deletedbouser.com").get());
        }

        boUserRepository.delete(boUser);
    }
}