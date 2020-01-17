package com.esther.dds.controller;

import com.esther.dds.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class AuthController {

    private UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String login(){
        return "redirect:/dashboard"; //pasaan: redirect/dashboard{id}

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


    //RegularMappings
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