package com.esther.dds.controller;

import com.esther.dds.automated.DatabaseFiller;
import com.esther.dds.domain.State;
import com.esther.dds.service.StateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class StateController {
    private static final Logger logger = LoggerFactory.getLogger(DemoController.class);


    private StateService stateService;
    private DatabaseFiller databaseFiller;

    public StateController(StateService stateService, DatabaseFiller databaseFiller) {
        this.stateService = stateService;
        this.databaseFiller = databaseFiller;
    }


    //     SET-TEXTS.HTML
    //todo: get texts + set texts
    @GetMapping("/admin/set-texts")
    public String loadTextForms(Model model){

        model.addAttribute("pending", stateService.findByStateName("Pending"));
        model.addAttribute("success", stateService.findByStateName("Success"));
        model.addAttribute("reject", stateService.findByStateName("Reject"));
        return "bo/a_set-texts";
    }



    //     SET-TEXTS.HTML
    //post stuff
    @PostMapping("/admin/set-texts/{state}")
    public String updatePendingState(Model model, @PathVariable("state") String stateName){

        State state = stateService.findByStateName(stateName);

        state.setId(state.getId());
        state.setStateName(state.getStateName());

        stateService.save(state);

        //the only way to update this is to somehow get the value in th form
        //And use .setMessage on this particular State

        return "redirect:/admin/set-texts";
    }


}