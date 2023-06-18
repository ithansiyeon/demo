package com.example.demo.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping(value = {"/login","/"})
    public String loginPage() {
        return "/login";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {return "/error/403";}
}
