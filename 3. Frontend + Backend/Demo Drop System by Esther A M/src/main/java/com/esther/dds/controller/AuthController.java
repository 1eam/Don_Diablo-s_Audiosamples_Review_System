package com.esther.dds.controller;

import com.esther.dds.domain.Admin;
import com.esther.dds.domain.BoUser;
import com.esther.dds.domain.User;
import com.esther.dds.service.AdminService;
import com.esther.dds.service.BoUserService;
import com.esther.dds.service.ProfileImageService;
import com.esther.dds.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Optional;

/**
 * ControllerClass that handles the http requests around Authentication & Authorization.
 * For example registration-process and login.
 * The string return-types represent the html page to be served to the user
 * (html templates are found in java/resources/templates)
 */

@Controller
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private UserService userService;
    private BoUserService boUserService;
    private AdminService adminService;
    private ProfileImageService profileImageService;


    public AuthController(UserService userService, BoUserService boUserService, AdminService adminService, ProfileImageService profileImageService) {
        this.userService = userService;
        this.boUserService = boUserService;
        this.adminService = adminService;
        this.profileImageService = profileImageService;
    }
//UserAuthentication & Authorization
    @GetMapping("/")
    public String toDashboard(Model model){
        return "redirect:/user-side/authorized/dashboard";
    }

    @GetMapping("/user-side")
    public String toDashboard2(Model model){
        return "redirect:/user-side/authorized/dashboard";
    }

    @GetMapping("user-side/login")
    public String loginAndRegister(Model model){
        model.addAttribute("newUser", new User());
        return "login";
    }

    @GetMapping("/user-side/register")
    public String mobileRegister(Model model){
        model.addAttribute("newUser", new User());
        return "mobile-register-page";
    }

    @GetMapping("/bo-side")
    public String boLogin(){
        return "redirect:/bo-side/authorized/dashboard";
    }

    @GetMapping("/bo-side/login")
    public String boLogin2(){
        return "bo/login";
    }

    @GetMapping("/admin-side")
    public String adminLogin(){
        return "redirect:/admin-side/authorized/dashboard";
    }

    @GetMapping("admin-side/login")
    public String adminLogin2(){
        return "bo/a_login";
    }

//UserRegistration
    @PostMapping("user-side/register")
    public String registerUser(@Valid User user, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes, @RequestParam("profileImage") MultipartFile profileImage) {
        //Store profileimage
        try {
            profileImageService.saveProfileImage(user, profileImage);
        } catch (Exception e){
            e.printStackTrace();
            logger.error("Error saving ProfileImage");
        }

        //Register new user
        User newUser = userService.register(user);
        redirectAttributes
                .addAttribute("id",newUser.getId())
                .addFlashAttribute("success",true);

        //Log event
        logger.info("New user was saved successfully");

        return "redirect:/user-side/login";
    }

//UserActivation
    @GetMapping("/user-side/activate/{email}/{activationCode}")
    public String activateUser(@PathVariable String email, @PathVariable String activationCode) {
        Optional<User> user = userService.findByEmailAndActivationCode(email,activationCode);
        if( user.isPresent() ) {
            User newUser = user.get();
            newUser.setEnabled(true);
            newUser.setConfirmPassword(newUser.getPassword());
            userService.save(newUser);
            userService.sendWelcomeEmail(newUser);
        }
        return "activation-success";
    }

    @GetMapping("/bo-side/activate/{email}/{activationCode}")
    public String activateBoUser(@PathVariable String email, @PathVariable String activationCode) {
        Optional<BoUser> boUser = boUserService.findByEmailAndActivationCode(email,activationCode);
        if( boUser.isPresent() ) {
            BoUser newBoUser = boUser.get();
            newBoUser.setEnabled(true);
            newBoUser.setConfirmPassword(newBoUser.getPassword());
            boUserService.save(newBoUser);
        }
        return "redirect:/bo-side/login";
    }

    @GetMapping("/admin-side/activate/{email}/{activationCode}")
    public String activateAdminUser(@PathVariable String email, @PathVariable String activationCode) {
        Optional<Admin> admin = adminService.findByEmailAndActivationCode(email,activationCode);
        if( admin.isPresent() ) {
            Admin newAdmin = admin.get();
            newAdmin.setEnabled(true);
            newAdmin.setConfirmPassword(newAdmin.getPassword());
            adminService.save(newAdmin);
        }
        return "redirect:/admin-side/authorized/editPassword";
    }
}