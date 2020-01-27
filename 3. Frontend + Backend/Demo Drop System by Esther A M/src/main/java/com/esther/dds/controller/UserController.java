package com.esther.dds.controller;

import com.esther.dds.domain.User;
import com.esther.dds.service.ProfileImageService;
import com.esther.dds.service.UserService;
import com.sun.xml.bind.v2.runtime.output.SAXOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(DemoController.class);
    private UserService userService;
    private ProfileImageService profileImageService;

    public UserController(UserService userService, ProfileImageService profileImageService) {
        this.userService = userService;
        this.profileImageService = profileImageService;
    }



// USER SIDE
    @GetMapping("/user-side/authorized/settings")
    public String settings(Model model ){
        Long userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        Optional<User> optionalUser = userService.findById(userId);

        if( optionalUser.isPresent() ) {
            User user = optionalUser.get();
            model.addAttribute("user", user);
            return "settings";
        } else {
            return "settings";
        }
    }
//
//    @PostMapping("/user-side/authorized/settings")
//    public String editProfile(Model model){
//        Long userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
//        Optional<User> user = userService.findById(userId);
//        if( user.isPresent() ) {
//            userService.save(user.get());
//            return "redirect:/user-side/authorized/settings";
//        } else {
//            return "settings";
//        }
//
//    }

    @PostMapping("/user-side/authorized/settings")
    public String editProfile(Model model,
                              @RequestParam(value = "artistName", required = false)String artistName,
                              @RequestParam(value = "bio", required = false)String bio,
                              @RequestParam(value = "name", required = false)String name,
                              @RequestParam(value = "lastName", required = false)String lastName) {

        Long userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        Optional<User> optionalUser = userService.findById(userId);


        if( optionalUser.isPresent() ) {
            User user = optionalUser.get();

            user.setArtistName(artistName);
            user.setBio(bio);
            user.setName(name);
            user.setLastName(lastName);
            userService.update(user);
            return "redirect:/user-side/authorized/settings";
        } else {
            return "redirect:/user-side/authorized/dashboard";
        }
    }

    @PostMapping("/user-side/authorized/editPassword")
    public String editPassword(Model model,
                               @RequestParam(value = "oldPassword", required = false)String oldPassword,
                               @RequestParam(value = "password")String password,
                               @RequestParam(value = "confirmPassword")String confirmPassword){

        Long userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        Optional<User> optionalUser = userService.findById(userId);

        if( optionalUser.isPresent() ) {
            if (oldPassword.equals(confirmPassword)){
                User user = optionalUser.get();
                userService.editPassword(user, oldPassword, password, confirmPassword);

                return "redirect:/user-side/authorized/settings";
                }else {
                return "redirect:/user-side/authorized/dashboard";
            }
        } else {
            return "redirect:/user-side/authorized/dashboard";
        }
    }


// ADMIN SIDE
    @GetMapping("/admin-side/authorized/user-management")
    public String userManagement(Model model){
        model.addAttribute("users", userService.findAll());
        return "bo/a_user-management";
    }

    // Delete User
    @PostMapping("/admin-side/authorized/user-management/delete/{id}")
    public String deleteDemo(User user, @PathVariable Long id, Model model){
        userService.delete(user);
        return "redirect:/admin-side/authorized/user-management/"; //redirect /id/dash
    }

    @GetMapping("/admin-side/authorized/bo-management")
    public String boManagement(){
        return "bo/a_bo-management";
    }
}