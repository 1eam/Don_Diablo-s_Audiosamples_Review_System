package com.esther.dds.controller;

import com.esther.dds.automated.DatabaseFiller;
import com.esther.dds.domain.State;
import com.esther.dds.repositories.StateNameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.PostUpdate;

@Controller
public class StateController {
    private static final Logger logger = LoggerFactory.getLogger(DemoController.class);

    private StateNameRepository stateNameRepository;

    private DatabaseFiller databaseFiller;

    public StateController(StateNameRepository stateNameRepository, DatabaseFiller databaseFiller) {
        this.stateNameRepository = stateNameRepository;
        this.databaseFiller = databaseFiller;
    }

    //     SET-TEXTS.HTML
    //get stuff
    @GetMapping("/admin/set-texts")
    public String setTextMapping(Model model){
        State state1 = stateNameRepository.findByStateName("Pending");
        final Long id = state1.getId();

        model.addAttribute("state1", state1);

        state1.setMessage(state1.getMessage());
        return "bo/a_set-texts";
    }

    //     SET-TEXTS.HTML
    //post stuff
    @PostMapping("/admin/set-texts")
    public String updatePendingMessage( State state, Model model, RedirectAttributes redirectAttributes){

        stateNameRepository.save(state);

        return "redirect:/admin/set-texts";
    }
}
