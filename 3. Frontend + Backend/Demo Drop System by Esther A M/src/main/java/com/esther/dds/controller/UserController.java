package com.esther.dds.controller;

import com.esther.dds.domain.User;
import com.esther.dds.service.ProfileImageService;
import com.esther.dds.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(DemoController.class);
    private UserService userService;
    private ProfileImageService profileImageService;

    public UserController(UserService userService, ProfileImageService profileImageService) {
        this.userService = userService;
        this.profileImageService = profileImageService;
    }



    @GetMapping("admin/user-management")
    public String userManagement(Model model){
        model.addAttribute("users", userService.findAll());
        return "bo/a_user-management";
    }

    // Delete User
    @PostMapping("admin/user-management/delete/{id}")
    public String deleteDemo(User user, @PathVariable Long id, Model model){
        userService.delete(user);
        return "redirect:/admin/user-management/"; //redirect /id/dash
    }

    @GetMapping("admin/bo-management")
    public String boManagement(){
        return "bo/a_bo-management";
    }
}
