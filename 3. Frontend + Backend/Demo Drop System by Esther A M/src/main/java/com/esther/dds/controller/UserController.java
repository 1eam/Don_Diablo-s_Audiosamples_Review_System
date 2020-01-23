package com.esther.dds.controller;

import com.esther.dds.domain.User;
import com.esther.dds.service.ProfileImageService;
import com.esther.dds.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.MultipartConfigElement;
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






    @GetMapping("admin/user-management")
    public String userManagement(){
        return "bo/tables/a_user-management";
    }

    @GetMapping("admin/bo-management")
    public String boManagement(){
        return "bo/tables/a_bo-management";
    }
}
