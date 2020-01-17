package com.esther.dds.controller;

import com.esther.dds.domain.User;
import com.esther.dds.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

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
    public String loginAndRegister(Model model){
        model.addAttribute("user", new User());
        return "login";
    }

    @GetMapping("/activate/{email}/{activationCode}")
    public String activate(@PathVariable String email, @PathVariable String activationCode) {
        Optional<User> user = userService.findByEmailAndActivationCode(email,activationCode);
        if( user.isPresent() ) {
            User newUser = user.get();
            newUser.setEnabled(true);
            newUser.setConfirmPassword(newUser.getPassword());
            userService.save(newUser);
            userService.sendWelcomeEmail(newUser);
            return "activation-succes";
        }
        return "redirect:/";
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

    //coppy getmapping login over
    @GetMapping("/register")
    public String registerMobile(){
        return "register";

    }

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