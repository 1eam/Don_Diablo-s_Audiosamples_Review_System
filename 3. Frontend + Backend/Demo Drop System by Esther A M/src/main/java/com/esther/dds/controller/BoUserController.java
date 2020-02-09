package com.esther.dds.controller;

import com.esther.dds.domain.BoUser;
import com.esther.dds.service.BoUserService;
import com.esther.dds.service.DemoService;
import com.esther.dds.service.MailService;
import com.esther.dds.service.StateService;
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
    private DemoService demoService;
    private StateService stateService;
    private MailService mailService;

    public BoUserController(BoUserService boUserService, DemoService demoService, StateService stateService, MailService mailService){
        this.boUserService = boUserService;
        this.demoService = demoService;
        this.stateService = stateService;
        this.mailService = mailService;
        encoder = new BCryptPasswordEncoder();
    }

    @GetMapping("/bo-side/authorized/dashboard")
    public String dashboard(Model model ){
        Long boUserId = ((BoUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        Optional<BoUser> optionalBoUser = boUserService.findById(boUserId);

        if( optionalBoUser.isPresent() ) {
            BoUser boUser = optionalBoUser.get();
            model.addAttribute("boUser", boUser);
            return "bo/dashboard";
        } else {
            return "bo/dashboard";
        }
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

}