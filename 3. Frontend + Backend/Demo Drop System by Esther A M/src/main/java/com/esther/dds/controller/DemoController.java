package com.esther.dds.controller;

import com.esther.dds.domain.Demo;
import com.esther.dds.repositories.DemoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class DemoController {

    private DemoRepository demoRepository;

    public DemoController(DemoRepository demoRepository) {
        this.demoRepository = demoRepository;
    }

    // List of Demos on dashboard
    @GetMapping("/dashboard")
    public String userList(Model model){
        model.addAttribute("demos", demoRepository.findAll());
        return "/dashboard";
    }

    //Demo view
    @GetMapping("/demo/{id}")
    public String viewDemo (@PathVariable Long id, Model model){
        Optional<Demo> demo = demoRepository.findById(id);
        if( demo.isPresent() ) {
            model.addAttribute("demo",demo.get());
            model.addAttribute("success", model.containsAttribute("success"));
            return "/viewdemo";

        } else {
            return "redirect:/";
        }
    }










//
//    // CRUD
//
//    @PostMapping("/create")
//    public Demo create(@ModelAttribute Demo demo) {
//        return demoRepository.save(demo);
//    }
//
//    @GetMapping("/{id}")
//    public Optional<Demo> read(@PathVariable Long id) {
//        return demoRepository.findById(id);
//    }
//
//    @PutMapping("/{id}")
//    public Demo update(@PathVariable Long id, @ModelAttribute Demo demo) {
//        // get the id
//        return demoRepository.save(demo);
//    }
//
//    @DeleteMapping("/{id}")
//    public void delete(@PathVariable Long id) {
//        demoRepository.deleteById(id);
//    }
//
//
//
//
//    //u_referenceMapping
//    @GetMapping("/thymeleaf/u_reference")
//    public String reference(Model model){
//        model.addAttribute("pageTitle", "reference");
//        return "/thymeleaf/u_reference";
//    }

}
