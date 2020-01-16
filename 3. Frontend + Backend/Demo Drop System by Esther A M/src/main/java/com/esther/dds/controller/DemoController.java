package com.esther.dds.controller;

import com.esther.dds.automated.DatabaseFiller;
import com.esther.dds.domain.Demo;

import com.esther.dds.repositories.DemoRepository;
import com.esther.dds.service.AudioFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class DemoController {

    private static final Logger logger = LoggerFactory.getLogger(DemoController.class);

    private DemoRepository demoRepository;
    private DatabaseFiller databaseFiller;
    private AudioFileService audioFileService;


    public DemoController(DemoRepository demoRepository, DatabaseFiller databaseFiller, AudioFileService audioFileService) {
        this.demoRepository = demoRepository;
        this.databaseFiller = databaseFiller;
        this.audioFileService = audioFileService;
    }

    //     DASHBOARD.HTML
    // List of Demos
    @GetMapping("/dashboard")
    public String userSideList(Model model){
        model.addAttribute("demos", demoRepository.findAll());
        return "dashboard";
    }

    //     VIEWDEMO.HTML
    // Play
    @GetMapping("/demo/{id}")
    public String userSideDemo (@PathVariable Long id, Model model){
        Optional<Demo> demo = demoRepository.findById(id);
        if( demo.isPresent() ) {
            model.addAttribute("demo", demo.get());
            model.addAttribute("success", model.containsAttribute("success"));
            return "viewdemo";

        } else {
            return "redirect:/";
        }
    }

    //     VIEWDEMO.HTML -> DELETE
    // Delete Demo
    @PostMapping ("/demo/{id}/delete")
    public String deleteDemo(Demo demo, @PathVariable Long id, Model model){
        demoRepository.delete(demo);
        return "redirect:/dashboard";
    }



    //     DROPDEMO.HTML
    // Load new Demo-object in form
    @GetMapping("/dropdemo")
    public String newDemoForm(Model model){
        model.addAttribute("demo", new Demo());
        return "dropdemo";
    }


//    @PostMapping("/dropdemo/uploadAudio")
//    public String uploadFile(@RequestParam("audioFile") MultipartFile audioFile){
//
//        try {
//            audioFileService.saveAudio(audioFile);
//        } catch (Exception e){
//            e.printStackTrace();
//            logger.error("Error saving Audio");
//            return "redirect:/dropdemo";
//        }
//
//        // save multipart file to folder
//        // get path (string) of multipartfile
//
//        return "redirect:/dropdemo";
//    }


    //     DROPDEMO.HTML -> POST
    // Bind form loaded in to object
    @PostMapping("/dropdemo")
    public String uploadDemo(@Valid Demo demo, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes, @RequestParam("audioFile") MultipartFile audioFile) {
        // save multipart file to folder + the path
        try {
            audioFileService.saveAudio(demo, audioFile);
        } catch (Exception e){
            e.printStackTrace();
            logger.error("Error saving Audio");
        }


        // save uploaded demo (title, description.)
        demoRepository.save(demo);

        // assign this demo to pending state
        demo.setState(databaseFiller.state1);

        // save demo again (update: + state)
        demoRepository.save(demo);


        // save demo again (update: + fileLocation. Save complete)
        demoRepository.save(demo);

        //log event
        logger.info("New Demo was saved successfully");
        redirectAttributes
                .addAttribute("id",demo.getId())
                .addFlashAttribute("success",true);


        // Dit herlaad de mappenstruktuur
        // (Is de demo direct na upload niet zichtbaar? klik dan met je muis in intellij, en ga terug naar de browser
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "redirect:/demo/{id}";

    }


    //     BO - REVIEWLIST.HTML
    // List of Demos
    @GetMapping("/bo/review-list")
    public String boSideList(Model model){
        model.addAttribute("demos", demoRepository.findByStateStateName("Pending"));

        return "bo/review-list";
    }

    //     BO - REVIEW-MODE.HTML
    // Play
    @GetMapping("/review-mode/{id}")
    public String boSideDemo (@PathVariable Long id, Model model){
        Optional<Demo> demo = demoRepository.findById(id);
        if( demo.isPresent() ) {
            model.addAttribute("demo",demo.get());
            return "bo/review-mode";
        }else {
            return "redirect:/";
        }
    }

    //-----------------------------------------//
    @PostMapping("/submit-state")
    public String setState(@Valid Demo demo, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {

        model.addAttribute("demo",demo);{
            // assign this demo to pending state
            demo.setState(databaseFiller.state2);

            // save uploaded demo (title, description.)
            demoRepository.save(demo);



            //log event
            logger.info("New Demo was saved successfully");
            redirectAttributes
                    .addAttribute("id",demo.getId())
                    .addFlashAttribute("success",true);
            return "redirect:/review-mode";
        }
    }
    //-----------------------------------------//

    //     BO - DASHBOARD.HTML
    @GetMapping("/bo/dashboard")
    public String boDashboard(){
        return "bo/dashboard";
    }

    //     BO - HANDLED-LIST.HTML
    // List of Demos
    @GetMapping("/bo/handled-list")
    public String boSideList2(Model model){
        model.addAttribute("demos", demoRepository.findByStateStateName("Rejected"));
        return "bo/handled-list";
    }

    //     BO - HANDLED-MODE.HTML
    // Play
    @GetMapping("/handled-mode/{id}")
    public String boSideDemo2 (@PathVariable Long id, Model model){
        Optional<Demo> demo = demoRepository.findById(id);
        if( demo.isPresent() ) {
            model.addAttribute("demo",demo.get());
            return "bo/handled-mode";
        }else {
            return "redirect:/";
        }
    }

    //     BO - SENTLIST.HTML
    // List of Demos
    @GetMapping("/bo/sent-list")
    public String boSideList3(Model model){
        model.addAttribute("demos", demoRepository.findByStateStateName("Sent"));
        return "bo/sent-list";
    }

    //     BO - SENT-MODE.HTML
    // Play
    @GetMapping("/sent-mode/{id}")
    public String boSideDemo3 (@PathVariable Long id, Model model){
        Optional<Demo> demo = demoRepository.findById(id);
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
}