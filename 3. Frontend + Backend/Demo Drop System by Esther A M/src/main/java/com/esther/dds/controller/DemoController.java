package com.esther.dds.controller;

import com.esther.dds.DdsApplication;
import com.esther.dds.domain.Demo;
import com.esther.dds.domain.State;
import com.esther.dds.repositories.DemoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class DemoController {

    private static final Logger logger = LoggerFactory.getLogger(DemoController.class);



    private DemoRepository demoRepository;

    public DemoController(DemoRepository demoRepository) {
        this.demoRepository = demoRepository;
    }

    // List of Demos on dashboard
    @GetMapping("/dashboard")
    public String userList(Model model){
        model.addAttribute("demos", demoRepository.findAll());
        return "dashboard";
    }

    // Demo view
    @GetMapping("/demo/{id}")
    public String viewDemo (@PathVariable Long id, Model model){
        Optional<Demo> demo = demoRepository.findById(id);
        if( demo.isPresent() ) {
            model.addAttribute("demo",demo.get());
            model.addAttribute("success", model.containsAttribute("success"));
            return "viewdemo";

        } else {
            return "redirect:/";
        }
    }

    // Demo drop form
    @GetMapping("/dropdemo")
    public String newDemoForm(Model model){
        model.addAttribute("demo", new Demo());
        return "dropdemo";
    }

    @PostMapping("/dropdemo")
    public String createDemo(@Valid Demo demo, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if( bindingResult.hasErrors() ) {
            logger.info("Validation errors were found while submitting a new demo.");
            model.addAttribute("demo",demo); //keep data when error occurs & page refreshes
            return "dropdemo";
        } else {
            // save uploaded demo
            demoRepository.save(demo);
//            demo.setState(state1);
            demoRepository.save(demo);

            logger.info("New Demo was saved successfully");
            redirectAttributes
                    .addAttribute("id",demo.getId())
                    .addFlashAttribute("success",true);
            return "redirect:/demo/{id}";
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
