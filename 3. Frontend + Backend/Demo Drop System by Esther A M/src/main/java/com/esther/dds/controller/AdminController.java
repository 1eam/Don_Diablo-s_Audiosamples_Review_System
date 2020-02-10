package com.esther.dds.controller;

import com.esther.dds.domain.BoUser;
import com.esther.dds.domain.User;
import com.esther.dds.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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


@Controller
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(DemoController.class);
    private final BCryptPasswordEncoder encoder;
    private BoUserService boUserService;
    private UserService userService;
    private DemoService demoService;
    private StateService stateService;
    private MailService mailService;

    public AdminController(BoUserService boUserService, UserService userService, DemoService demoService, StateService stateService, MailService mailService){
        this.boUserService = boUserService;
        this.userService = userService;
        this.demoService = demoService;
        this.stateService = stateService;
        this.mailService = mailService;
        encoder = new BCryptPasswordEncoder();
    }



    @GetMapping("/admin-side/authorized/user-management")
    public String userManagement(Model model){
        model.addAttribute("users", userService.findAll());
        return "bo/a_user-management";
    }

    @GetMapping("/admin-side/authorized/bo-management")
    public String boManagement(Model model){
        model.addAttribute("boUsers", boUserService.findAll());
        model.addAttribute("newBoUser", new BoUser());
        return "bo/a_bo-management";
    }

    // Delete User
    @PostMapping("/admin-side/authorized/user-management/delete/{id}")
    public String deleteDemo(User user, @PathVariable Long id, Model model){
        userService.delete(user);
        return "redirect:/admin-side/authorized/user-management/"; //redirect /id/dash
    }

    // Delete Bo-User
    @PostMapping("/admin-side/authorized/bo-management/delete/{id}")
    public String deleteDemo(BoUser boUser, @PathVariable Long id, Model model){
        boUserService.delete(boUser);
        return "redirect:/admin-side/authorized/bo-management/";
    }

    // Create new Bo-User
    @PostMapping("/admin-side/authorized/bo-management/createBoUser")
    public String registerBoUser(BoUser boUser, RedirectAttributes redirectAttributes) {
        // Register new bo-user
        BoUser newBoUser = boUserService.register(boUser);
        redirectAttributes
            .addAttribute("id",newBoUser.getId())
            .addFlashAttribute("succesCreatingBoUser",true);
        //log event
        logger.info("New user was saved successfully");

        return "redirect:/admin-side/authorized/bo-management";

    }

}