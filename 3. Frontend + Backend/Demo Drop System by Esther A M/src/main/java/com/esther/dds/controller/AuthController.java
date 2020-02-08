package com.esther.dds.controller;

import com.esther.dds.domain.User;
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

@Controller
public class AuthController {

    private UserService userService;
    private ProfileImageService profileImageService;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    public AuthController(UserService userService, ProfileImageService profileImageService) {
        this.userService = userService;
        this.profileImageService = profileImageService;
    }

    @GetMapping("/")
    public String toDashboard(Model model){
        return "redirect:/user-side/authorized/dashboard"; //pas aan:
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
    public String registerMobile(Model model){
        model.addAttribute("newUser", new User());
        return "register";
    }

    @PostMapping("user-side/register")
    public String registerUser(@Valid User user, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes, @RequestParam("profileImage") MultipartFile profileImage) {

        // save multipart file to folder + set the path
        try {
            profileImageService.saveProfileImage(user, profileImage);
        } catch (Exception e){
            e.printStackTrace();
            logger.error("Error saving ProfileImage");
        }

        // Register new user
        User newUser = userService.register(user);
        redirectAttributes
                .addAttribute("id",newUser.getId())
                .addFlashAttribute("success",true);

        //log event
        logger.info("New user was saved successfully");

        return "redirect:/user-side/login";

    }

    @GetMapping("/user-side/activate/{email}/{activationCode}")
    public String activate(@PathVariable String email, @PathVariable String activationCode) {
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


    //RegularMappings
    @GetMapping("/bo-side")
    public String boLogin2(){
        return "redirect:/bo-side/authorized/dashboard";
    }

    @GetMapping("/bo-side/login")
    public String boLogin(){
        return "bo/login";
    }

    @GetMapping("admin-side/login")
    public String adminLogin(){
        return "bo/login";
    }

    @GetMapping("admin-side/authorized/dashboard")
    public String adminDashboard(){
        return "bo/a_dashboard";
    }

}