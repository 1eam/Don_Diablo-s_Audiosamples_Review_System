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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(DemoController.class);
    private UserService userService;
    private ProfileImageService profileImageService;

    public UserController(UserService userService, ProfileImageService profileImageService) {
        this.userService = userService;
        this.profileImageService = profileImageService;
    }

    @PostMapping("/register") //getmapping zit in Auth
    public String registerUser(@Valid User user, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes, @RequestParam("profileImage") MultipartFile profileImage) {

        if( bindingResult.hasErrors() ) {
            // show validation errors
            logger.info("Validation errors were found while registering a new user");
            model.addAttribute("user",user);
            model.addAttribute("validationErrors", bindingResult.getAllErrors());
            return "login";
        } else {

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

            // Dit herlaad de mappenstructuur
            // (Is de afbeelding toch niet zichtbaar? klik dan met je muis in intellij, en ga terug naar de browser. of refresh de map
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return "redirect:/login";

        }
    }


    @GetMapping("admin/user-management")
    public String userManagement(){
        return "bo/tables/a_user-management";
    }

    @GetMapping("admin/bo-management")
    public String boManagement(){
        return "bo/tables/a_bo-management";
    }
}
