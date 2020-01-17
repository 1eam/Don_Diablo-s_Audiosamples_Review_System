package com.esther.dds.controller;

import com.esther.dds.automated.DatabaseFiller;
import com.esther.dds.domain.State;
import com.esther.dds.service.StateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
    //get stuff
    @GetMapping("/admin/set-texts")
    public String loadText1Form(Model model){
        model.addAttribute("state", stateService.findByStateName("Pending"));
        return "bo/a_set-texts";
    }




    //     SET-TEXTS.HTML
    //post stuff
    @PostMapping("/admin/set-texts/{stateName}")
    public String updatePendingState(Model model, @PathVariable String stateName, @RequestBody String requestbody){

        State state = stateService.findByStateName(stateName);




        state.setMessage(requestbody);
        stateService.save(state);

        //the only way to update this is to somehow get the value in th form
        //And use .setMessage on this particular State
        return "redirect:/admin/set-texts";
    }


}