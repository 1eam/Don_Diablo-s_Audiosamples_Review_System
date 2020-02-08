package com.esther.dds.controller;

import com.esther.dds.domain.BoUser;
import com.esther.dds.service.BoUserService;
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
public class BoUserController {

    private static final Logger logger = LoggerFactory.getLogger(DemoController.class);
    private final BCryptPasswordEncoder encoder;
    private BoUserService boUserService;

    public BoUserController(BoUserService boUserService){
        this.boUserService = boUserService;
        encoder = new BCryptPasswordEncoder();
    }

    @GetMapping("/bo-side/authorized/editPassword")
    public String settings(Model model ){
        Long boUserId = ((BoUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        Optional<BoUser> optionalBoUser = boUserService.findById(boUserId);

        if( optionalBoUser.isPresent() ) {
            BoUser boUser = optionalBoUser.get();
            model.addAttribute("boUser", boUser);
            return "bo/edit-password";
        } else {
            return "bo/edit-password";
        }
    }

    @PostMapping("/bo-side/authorized/editPassword")
    public String editPassword(Model model, RedirectAttributes redirectAttributes,
                               @RequestParam(value = "oldPassword", required = false)String oldPassword,
                               @RequestParam(value = "password")String password,
                               @RequestParam(value = "confirmPassword")String confirmPassword){

        Long boUserId = ((BoUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        Optional<BoUser> optionalBoUser = boUserService.findById(boUserId);

        if( optionalBoUser.isPresent() ) {
            BoUser boUser = optionalBoUser.get();

            String currentPassword = boUser.getPassword();
            //this removes the "{bcrypt}" prefix. This has to be done first. in order for BCrypts .matches method to work
            String currentPwWithoutPrefix = currentPassword.substring(8);
            //send incorrect password message if false
            if(!encoder.matches(oldPassword, currentPwWithoutPrefix)){
                redirectAttributes
                        .addFlashAttribute("passwordIncorrect",true);
                return "redirect:/bo-side/authorized/editPassword";
            }

            //send passwords dont match message if false
            if (password.equals(confirmPassword)){
                boUserService.editPassword(boUser, oldPassword, password);

                return "redirect:/bo-side/authorized/dashboard";

                } else {
                redirectAttributes
                        .addFlashAttribute("confirmFailed",true);
                return "redirect:/bo-side/authorized/editPassword";
            }
        } else {
            redirectAttributes
                    .addFlashAttribute("incorrectPassword",true);
            return "redirect:/bo-side/authorized/editPassword";
        }
    }

    //~~ADMIN SIDE~~

    @PostMapping("/bo-side/authorized/deleteAccount")
    public String editPassword(Model model,
                               @RequestParam(value = "password")String password){

        Long boUserId = ((BoUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        Optional<BoUser> optionalBoUser = boUserService.findById(boUserId);
        BoUser boUser = optionalBoUser.get(); //refactor in if statement

        //this removes the "{bcrypt}" prefix. This has to be done first. in order for BCrypts .matches method to work
        String currentPassword = boUser.getPassword();
        String currentPwWithoutPrefix = currentPassword.substring(8);

        if( optionalBoUser.isPresent() && encoder.matches(password, currentPwWithoutPrefix) ) {
            boUserService.delete(boUser);
            SecurityContextHolder.getContext().setAuthentication(null);
            return "redirect:/bo-side/authorized/dashboard";
        } else {
            return "redirect:/bo-side/authorized/dashboard";
        }
    }

    @GetMapping("/admin-side/authorized/bo-management")
    public String boManagement(Model model){
        model.addAttribute("boUsers", boUserService.findAll());
        return "bo/a_bo-management";
    }

    // Delete BoUser
    @PostMapping("/admin-side/authorized/bo-management/delete/{id}")
    public String deleteDemo(BoUser boUser, @PathVariable Long id, Model model){
        boUserService.delete(boUser);
        return "redirect:/admin-side/authorized/bo-management/"; //redirect /id/dash
    }

}