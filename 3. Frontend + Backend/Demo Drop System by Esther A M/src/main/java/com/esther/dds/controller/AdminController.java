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
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(DemoController.class);
    private final BCryptPasswordEncoder encoder;
    private BoUserService boUserService;
    private DemoService demoService;
    private StateService stateService;
    private MailService mailService;

    public AdminController(BoUserService boUserService, DemoService demoService, StateService stateService, MailService mailService){
        this.boUserService = boUserService;
        this.demoService = demoService;
        this.stateService = stateService;
        this.mailService = mailService;
        encoder = new BCryptPasswordEncoder();
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
        return "redirect:/admin-side/authorized/bo-management/";
    }

}