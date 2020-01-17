package com.esther.dds.controller;

import com.esther.dds.domain.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    @GetMapping("/")
    public String login(){
        return "dashboard";

    }

    @GetMapping("/login")
    public String authenticate(){
        return "login";

    }

    @GetMapping("/register")
    public String registerMobile(){
        return "register";

    }

//    @PostMapping("/login")
//    public String loginPost() {
//        return "dashboard";
//    }

    //WHEN USERS ARE CREATED
//    @PostMapping("/login/{id}")
//    public String loginPost(@PathVariable User id) {
//        //user.findbyId
//        return "dashboard";
//
//    }



    @GetMapping("/settings/{id}")
    public String settings(@PathVariable Long id){
        return "settings";
    }

}