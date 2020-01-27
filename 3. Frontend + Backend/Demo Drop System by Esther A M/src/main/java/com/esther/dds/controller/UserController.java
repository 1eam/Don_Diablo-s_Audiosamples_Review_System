package com.esther.dds.controller;

import com.esther.dds.domain.User;
import com.esther.dds.service.ProfileImageService;
import com.esther.dds.service.UserService;
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
        User user = optionalUser.get();

        if( optionalUser.isPresent() ) {
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
        User user = optionalUser.get();

        if( optionalUser.isPresent() ) {

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