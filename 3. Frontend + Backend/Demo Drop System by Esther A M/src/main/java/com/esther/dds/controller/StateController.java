package com.esther.dds.controller;

import com.esther.dds.domain.State;
import com.esther.dds.service.StateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class StateController {
    private static final Logger logger = LoggerFactory.getLogger(DemoController.class);
    private StateService stateService;

    public StateController(StateService stateService) {
        this.stateService = stateService;
    }
    //     SET-TEXTS.HTML
    @GetMapping("/admin-side/authorized/set-texts")
    public String loadTextForms(Model model){
        model.addAttribute("pending", stateService.findByStateName("Pending"));
        model.addAttribute("sent", stateService.findByStateName("Sent"));
        model.addAttribute("rejected", stateService.findByStateName("Rejected"));
        return "bo/a_set-texts";
    }

    //     SET-TEXTS.HTML
    @PostMapping("/admin-side/authorized/set-texts/{state}")
    public String updatePendingState(Model model, @PathVariable("state") String stateName, @RequestParam(value = "message")String message){
        State state = stateService.findByStateName(stateName);
        state.setMessage(message);
        stateService.save(state);
        logger.info("state message: '" + stateName + "' was updated successfully");
        return "redirect:/admin-side/authorized/set-texts";
    }
}