package com.esther.dds.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping("admin/user-management")
    public String userManagement(){
        return "bo/tables/a_user-management";
    }

    @GetMapping("admin/bo-management")
    public String boManagement(){
        return "bo/tables/a_bo-management";
    }
}
