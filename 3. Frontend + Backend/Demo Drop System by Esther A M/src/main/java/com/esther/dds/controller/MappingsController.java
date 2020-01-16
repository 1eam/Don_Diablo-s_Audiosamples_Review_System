package com.esther.dds.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MappingsController {

    @GetMapping("/bo/login")
    public String boLogin(){
        return "bo/login";
    }

    @GetMapping("admin/login")
    public String adminLogin(){
        return "bo/login";
    }

    @GetMapping("admin/dashboard")
    public String adminDashboard(){
        return "bo/a_dashboard";
    }






}
