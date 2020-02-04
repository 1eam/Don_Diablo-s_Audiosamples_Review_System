package com.esther.dds.controller;

import com.esther.dds.automated.DatabaseFiller;
import com.esther.dds.domain.Demo;



import com.esther.dds.domain.User;
import com.esther.dds.repositories.DemoRepository;
import com.esther.dds.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Controller
public class DemoController {

    private static final Logger logger = LoggerFactory.getLogger(DemoController.class);

    private UserService userService;
    private DemoService demoService;
    private DemoRepository demoRepository;
    private DatabaseFiller databaseFiller;
    private AudioFileService audioFileService;
    private MailService mailService;
    private StateService stateService;

    public DemoController(UserService userService, DemoService demoService, DemoRepository demoRepository, DatabaseFiller databaseFiller, AudioFileService audioFileService, MailService mailService, StateService stateService){
        this.userService = userService;
        this.demoService = demoService;
        this.demoRepository = demoRepository;
        this.databaseFiller = databaseFiller;
        this.audioFileService = audioFileService;
        this.mailService = mailService;
        this.stateService = stateService;

    }

    //     DASHBOARD.HTML
    // List of Demos
    @GetMapping("/user-side/authorized/dashboard")
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

    //     DROPDEMO.HTML
    // Load new Demo-object in form
    @GetMapping("/user-side/authorized/dropdemo")
    public String newDemoForm(Model model, RedirectAttributes redirectAttributes){

        Long userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        Optional<User> user = userService.findById(userId);

        //check if user hasnt reached the maximum of 2 uploads in review = in Pending-state
        if (demoRepository.findByUserIdAndStateStateName(userId, "Pending").size()==2){

            redirectAttributes
                    .addFlashAttribute("maxReached",true);
            return "redirect:/user-side/authorized/dashboard";

        } else {
        //show the upload demo-form
        model.addAttribute("demo", new Demo());
        return "dropdemo";
        }
    }


    //     DROPDEMO.HTML -> POST
    // Bind form loaded in to object
    @PostMapping("/user-side/authorized/dropdemo")
    public String uploadDemo(@Valid Demo demo, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes, @RequestParam("audioFile") MultipartFile audioFile) {
        // save multipart file to folder + the path
        try {
            audioFileService.saveAudio(demo, audioFile);
        } catch (Exception e){
            e.printStackTrace();
            logger.error("Error saving Audio");
        }

        // save uploaded demo (title, description.)
        demoService.save(demo);

        // assign this demo to pending state
        demo.setState(databaseFiller.state1);

        // save demo again (update: + state)
        demoService.save(demo);

        // set upload to current user
        Long userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        Optional<User> user = userService.findById(userId);
        demo.setUser(user.get());

        // save demo again (applied user)
        demoService.save(demo);

        //log event
        logger.info("New Demo was saved successfully");
        redirectAttributes
                .addAttribute("id",demo.getId())
                .addFlashAttribute("success",true);

        return "redirect:/user-side/authorized/demo/{id}";
    }

    //     VIEWDEMO.HTML
    // Play
    @GetMapping("/user-side/authorized/demo/{id}")
    public String userSideDemo (@PathVariable Long id, Model model){
        Optional<Demo> demo = demoService.findById(id);
        if( demo.isPresent() ) {
            model.addAttribute("demo", demo.get());
            model.addAttribute("success", model.containsAttribute("success"));
            return "viewdemo";

        } else {
            return "redirect:/user-side/authorized/dashboard";
        }
    }

    //     VIEWDEMO.HTML -> DELETE
    // Delete Demo
    @PostMapping ("/user-side/authorized/demo/{id}/delete")
    public String deleteDemo(Demo demo, @PathVariable Long id, Model model){
        demoService.delete(demo);
        return "redirect:/user-side/authorized/dashboard"; //redirect /id/dash
    }


    //     BO - REVIEWLIST.HTML
    // List of Demos
    @GetMapping("/bo-side/authorized/review-list")
    public String boSideList(Model model){
        model.addAttribute("demos", demoService.findByStateStateName("Pending"));

        return "bo/review-list";
    }

    //     BO - REVIEW-MODE.HTML
    // Show review-mode, or redirect to list-view if all demos are reviewed
    @GetMapping("/bo-side/authorized/review-mode/{id}")
    public String boSideDemo (@PathVariable Long id, Model model){
        Optional<Demo> demo = demoService.findById(id);

        // !Important, execute only if the (pathVariable) Demo's state is equal to pending (else any demo-url can be re-judged afterwards)
        if( demo.isPresent() && demo.get().getState().getStateName()=="Pending") {
            model.addAttribute("demo",demo.get());
            return "bo/review-mode";
        }else {
            return "redirect:/bo-side/authorized/review-list";
        }
    }

    //----------------------------------------------------------------------------------------------------------------//
    //     BO - REVIEW-MODE.HTML
    // Cast review & redirect to next demo

    @PostMapping("/bo-side/authorized/submit-review/{id}")
    public String setState(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes,
                           @RequestParam(value = "state.stateName", required = false) String state){

        Optional<Demo> optionalDemo = demoService.findById(id);
        if (optionalDemo.isPresent()){

            Demo demo = optionalDemo.get();

//          redundant variable moet voor een switch statement
            String stateOutcome = state;
            switch (stateOutcome) {
                case "Rejected":
    //              assign this demo to pending state
                    demo.setState(stateService.findByStateName("Rejected"));
                    //todo: set reviewer to user: securitycontextholder
                    demoService.save(demo);

    //              Send email notifying user about demo's rejection state
                    mailService.sendRejectionEmail(demo.getUser(), demo);

    //              log event
                    logger.info("New Demo was successfully assigned to state: 'Rejected'");
                    break;

                case "Sent":
    //              assign this demo to sent state
                    demo.setState(stateService.findByStateName("Sent"));
                    //todo: set reviewer
                    demoService.save(demo);

    //              log event
                    logger.info("New Demo was successfully assigned to state: 'Sent'");

    //              Send email notifying user about demo's state being forwarded to DD
                    mailService.sendForwardedEmail(demo.getUser(), demo);
                    break;

                default:
                    logger.info("Demo is still assigned to state 'Pending'");
                    break;
            }
        }

        List<Demo> list = demoService.findByStateStateName("Pending");

//      to prevent exceptions if the list of demo's get Empty
        if(list.size() >= 1){
            String nextDemoId = list.get(0).getId().toString();
            return "redirect:/bo-side/authorized/review-mode/" + nextDemoId;
        } else{
            return "redirect:/bo-side/authorized/review-list";
        }
    }
    //----------------------------------------------------------------------------------------------------------------//

    //     BO - DASHBOARD.HTML
    @GetMapping("/bo-side/authorized/dashboard")
    public String boDashboard(){
        return "bo/dashboard";
    }

    //     BO - HANDLED-LIST.HTML
    // List of Demos
    @GetMapping("/bo-side/authorized/handled-list")
    public String boSideList2(Model model){
        //purpose: find all demos containing state Rejected and Sent, and push them to the model
        List<Demo> rejectedDemos = demoService.findByStateStateName("Rejected");
        List<Demo> sentDemos = demoService.findByStateStateName("Sent");
        //store both lists in one list
        List<Demo> demos = new ArrayList<>();
        Stream.of(rejectedDemos, sentDemos).forEach(demos::addAll);
        //sort the demos by "ReviewedOn" (=LastModifiedDate)
        demos.sort(Comparator.comparing(Demo::getReviewedOn).reversed());
        //pass list of demos to view
        model.addAttribute("demos", demos);
        return "bo/handled-list";
    }

    //     BO - HANDLED-MODE.HTML
    // Play
    @GetMapping("/bo-side/authorized/handled-mode/{id}")
    public String boSideDemo2 (@PathVariable Long id, Model model){
        Optional<Demo> demo = demoService.findById(id);
        if( demo.isPresent() ) {
            model.addAttribute("demo",demo.get());
            return "bo/handled-mode";
        }else {
            return "redirect:/";
        }
    }

    //     BO - SENTLIST.HTML
    // List of Demos
    @GetMapping("/bo-side/authorized/sent-list")
    public String boSideList3(Model model){
        model.addAttribute("demos", demoService.findByStateStateName("Sent"));
        return "bo/sent-list";
    }

    //     BO - SENT-MODE.HTML
    // Play
    @GetMapping("/bo-side/authorized/sent-mode/{id}")
    public String boSideDemo3 (@PathVariable Long id, Model model){
        Optional<Demo> demo = demoService.findById(id);
        if( demo.isPresent() ) {
            model.addAttribute("demo",demo.get());
            return "bo/sent-mode";
        }else {
            return "redirect:/";
        }
    }




    //     ADMIN - REVIEW-MODE.HTML




//    //REFERENCE
//
//    @PostMapping("/create")
//    public Demo create(@ModelAttribute Demo demo) {
//        return demoService.save(demo);
//    }
//
//    @GetMapping("/{id}")
//    public Optional<Demo> read(@PathVariable Long id) {
//        return demoService.findById(id);
//    }
//
//    @PutMapping("/{id}")
//    public Demo update(@PathVariable Long id, @ModelAttribute Demo demo) {
//        // get the id
//        return demoService.save(demo);
//    }
//
//    @DeleteMapping("/{id}")
//    public void delete(@PathVariable Long id) {
//        demoService.deleteById(id);
//    }
}