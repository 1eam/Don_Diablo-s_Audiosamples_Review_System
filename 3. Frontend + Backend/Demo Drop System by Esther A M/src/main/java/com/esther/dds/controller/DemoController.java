package com.esther.dds.controller;

import com.esther.dds.domain.Demo;
import com.esther.dds.repositories.DemoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class DemoController {
    private DemoRepository demoRepository;

    public DemoController(DemoRepository demoRepository) {
        this.demoRepository = demoRepository;
    }

    // list of Demos

    @GetMapping("/")
    public List<Demo> list() {
        return demoRepository.findAll();
    }


















    // CRUD

    @PostMapping("/create")
    public Demo create(@ModelAttribute Demo demo) {
        return demoRepository.save(demo);
    }

    @GetMapping("/{id}")
    public Optional<Demo> read(@PathVariable Long id) {
        return demoRepository.findById(id);
    }

    @PutMapping("/{id}")
    public Demo update(@PathVariable Long id, @ModelAttribute Demo demo) {
        // get the id
        return demoRepository.save(demo);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        demoRepository.deleteById(id);
    }


    //u_reference
    @GetMapping("/thymeleaf/u_reference")
    public String foo(Model model){
        model.addAttribute("pageTitle", "reference");
        return "/thymeleaf/u_reference";
    }

}
