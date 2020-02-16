package com.esther.dds.controller;

import com.esther.dds.domain.Admin;
import com.esther.dds.domain.BoUser;
import com.esther.dds.domain.User;
import com.esther.dds.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;


@Controller
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(DemoController.class);
    private final BCryptPasswordEncoder encoder;
    private UserService userService;
    private BoUserService boUserService;
    private AdminService adminService;
    private DemoService demoService;
    private StateService stateService;
    private MailService mailService;

    public AdminController(UserService userService, BoUserService boUserService, AdminService adminService, DemoService demoService, StateService stateService, MailService mailService){
        this.userService = userService;
        this.boUserService = boUserService;
        this.adminService = adminService;
        this.demoService = demoService;
        this.stateService = stateService;
        this.mailService = mailService;
        encoder = new BCryptPasswordEncoder();
    }


    @GetMapping("admin-side/authorized/dashboard")
    public String adminDashboard(){
        return "bo/a_dashboard";
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

    //Edit Password
    @GetMapping("/admin-side/authorized/editPassword")
    public String settings(Model model ){
        Long adminId = ((Admin) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        Optional<Admin> optionalAdmin = adminService.findById(adminId);

        if( optionalAdmin.isPresent() ) {
            Admin admin = optionalAdmin.get();
            model.addAttribute("admin", admin);
            return "bo/a_edit-password";
        } else {
            return "bo/a_edit-password";
        }
    }

    @PostMapping("/admin-side/authorized/editPassword")
    public String editPassword(Model model, RedirectAttributes redirectAttributes,
                               @RequestParam(value = "oldPassword", required = false)String oldPassword,
                               @RequestParam(value = "password")String password,
                               @RequestParam(value = "confirmPassword")String confirmPassword){

        Long adminId = ((Admin) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        Optional<Admin> optionalAdmin = adminService.findById(adminId);

        if( optionalAdmin.isPresent() ) {
            Admin admin = optionalAdmin.get();

            String currentPassword = admin.getPassword();
            //this removes the "{bcrypt}" prefix. This has to be done first. in order for BCrypts .matches method to work
            String currentPwWithoutPrefix = currentPassword.substring(8);
            //send incorrect password message if false
            if(!encoder.matches(oldPassword, currentPwWithoutPrefix)){
                redirectAttributes
                        .addFlashAttribute("passwordIncorrect",true);
                return "redirect:/admin-side/authorized/editPassword";
            }

            //send passwords dont match message if false
            if (password.equals(confirmPassword)){
                adminService.editPassword(admin, oldPassword, password);

                return "redirect:/admin-side/authorized/dashboard";

            } else {
                redirectAttributes
                        .addFlashAttribute("confirmFailed",true);
                return "redirect:/admin-side/authorized/editPassword";
            }
        } else {
            redirectAttributes
                    .addFlashAttribute("incorrectPassword",true);
            return "redirect:/admin-side/authorized/editPassword";
        }
    }

}