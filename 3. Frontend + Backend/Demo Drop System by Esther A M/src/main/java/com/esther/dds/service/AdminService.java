package com.esther.dds.service;

import com.esther.dds.domain.Admin;
import com.esther.dds.domain.Demo;
import com.esther.dds.repositories.AdminRepository;
import com.esther.dds.repositories.DemoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AdminService {

    private final Logger logger = LoggerFactory.getLogger(AdminService.class);
    private final AdminRepository adminRepository;
    private final BCryptPasswordEncoder encoder;
    private final AdminRoleService adminRoleService;
    private final MailService mailService;
    private DemoRepository demoRepository;

    public AdminService(AdminRepository adminRepository, AdminRoleService adminRoleService, MailService mailService, DemoRepository demoRepository) {
        this.adminRepository = adminRepository;
        this.adminRoleService = adminRoleService;
        this.mailService = mailService;
        this.demoRepository = demoRepository;
        //make new encoder every time
        encoder = new BCryptPasswordEncoder();
    }

    public Optional<Admin> findById(Long id) {
        return adminRepository.findById(id);
    }

    public Optional<Admin> findByEmail(String email){
        return adminRepository.findByEmail(email);
    }

    public List<Admin> findAll() {
        return adminRepository.findAll();
    }

    public Admin save(Admin admin) {
        return adminRepository.save(admin);
    }

    public Admin register(Admin admin) {
        // disable the admin account
        admin.setEnabled(false);

        // generate password using 32 digit UUID, and trim beginning by 24, so the result will be a 12 digit - password
        // the value stored in "(set)generatedPassword" will be called again in the activation email for the admin user (generatedPassword is a transient object)
        String generatedPassword = UUID.randomUUID().toString().substring(24);
        admin.setGeneratedPassword(generatedPassword);

        // take generated password and encode, then set the password to password
        String secret = "{bcrypt}" + encoder.encode(generatedPassword);
        admin.setPassword(secret);

        // confirm password
        admin.setConfirmPassword(secret);

        // assign a role to this admin-user
        admin.addAdminRole(adminRoleService.findByName("ROLE_ADMIN-USER")); //check

        // set activation code
        admin.setActivationCode(UUID.randomUUID().toString());

        // save admin-user
        save(admin);

        // send activation email
        sendActivationEmail(admin);

        // return admin-user (todo: Review code. This method can probably return void)
        return admin;
    }

    public void sendActivationEmail(Admin admin) {
        mailService.sendAdminActivationEmail(admin);
    }

    public Optional<Admin> findByEmailAndActivationCode(String email, String activationCode) {
        return adminRepository.findByEmailAndActivationCode(email,activationCode);
    }

    public Admin update(Admin admin) {
        // first lets stop confirmPassword from complaining using the following code
        admin.setPassword(admin.getPassword());
        admin.setConfirmPassword(admin.getPassword());

        // actual update function
        return adminRepository.save(admin);
    }


    public Admin editPassword(Admin admin, String oldPassword, String password) {
        String currentPassword = admin.getPassword();
        //this removes the "{bcrypt}" text prefix in the db. This has to be done first. in order for BCrypts .matches method to work
        String currentPwWithoutPrefix = currentPassword.substring(8);

        if( encoder.matches(oldPassword, currentPwWithoutPrefix)){
            //Edit the new password
            String secret = "{bcrypt}" + encoder.encode(password);
            admin.setPassword(secret);

            // setconfirm password field
            admin.setConfirmPassword(secret);
            adminRepository.save(admin);

        } else {
            logger.error("incorrect password, try again: ");

        }

        return admin;
    }

    // Just in case for future admin-accounts to be deleted
    public void delete(Admin admin) {
        adminRepository.delete(admin);
    }
}