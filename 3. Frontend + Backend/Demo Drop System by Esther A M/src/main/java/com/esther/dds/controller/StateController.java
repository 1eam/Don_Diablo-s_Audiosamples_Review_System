package com.esther.dds.controller;

import com.esther.dds.repositories.StateNameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StateController {
    private static final Logger logger = LoggerFactory.getLogger(DemoController.class);

    private StateNameRepository stateNameRepository;

    public StateController(StateNameRepository stateNameRepository) {
        this.stateNameRepository = stateNameRepository;
    }

    //     DASHBOARD.HTML
    // List of Demos
    @GetMapping("/admin/set-texts")
    public String setTextsMapping(Model model){
        model.addAttribute("state", stateNameRepository.findAllByStateName());
        return "bo/a_set-texts";
    }
}
