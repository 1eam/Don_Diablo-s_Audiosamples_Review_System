package com.esther.dds.controller;

import com.esther.dds.domain.User;
import com.esther.dds.repositories.UserRepository;
import com.esther.dds.service.DemoService;
import com.esther.dds.service.ProfileImageService;
import com.esther.dds.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
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
import java.util.Optional;

@Controller
public class AuthController {

    private UserService userService;
    private UserRepository userRepository;
    private DemoService demoService;
    private ProfileImageService profileImageService;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    public AuthController(UserService userService, UserRepository userRepository, DemoService demoService, ProfileImageService profileImageService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.demoService = demoService;
        this.profileImageService = profileImageService;
    }

    @GetMapping("/")
    public String toDashboard(Model model){
        return "redirect:/dashboard"; //pas aan:
    }


    @GetMapping("/login")
    public String loginAndRegister(Model model){
        model.addAttribute("newUser", new User());
        return "login";
    }

//    @PostMapping("/login")
//    public String dashboard(Model model, @PathVariable Long id){
//        Optional<User> user = userService.findById(id);
//        model.addAttribute("user", user);
//        return "redirect:/{id}/dashboard";
//    }

    @GetMapping("/dashboard")
    public String userSideDemo (Model model){
        Long userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        Optional<User> user = userService.findById(userId);

        if( user.isPresent() ) {
            model.addAttribute("user", user.get());
            model.addAttribute("demos", demoService.findByUser(userId));
            model.addAttribute("success", model.containsAttribute("success"));
            return "dashboard";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/register") //getmapping zit in Auth
    public String registerUser(@Valid User user, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes, @RequestParam("profileImage") MultipartFile profileImage) {

        // save multipart file to folder + set the path
        try {
            profileImageService.saveProfileImage(user, profileImage);
        } catch (Exception e){
            e.printStackTrace();
            logger.error("Error saving ProfileImage");
        }

        // Register new user
        User newUser = userService.register(user);
        redirectAttributes
                .addAttribute("id",newUser.getId())
                .addFlashAttribute("success",true);

        //log event
        logger.info("New user was saved successfully");



        // Dit herlaad de mappenstructuur
        // (Is de afbeelding toch niet zichtbaar? klik dan met je muis in intellij, en ga terug naar de browser. of refresh de map
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "redirect:/login";

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
            return "activation-success";
        }
        return "redirect:/";
    }




//    @PostMapping("/login")
//    public String loginPost() {
//        return "dashboard";
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