package com.samuelangan.mycompagny.authentification.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class userPageController {
    @GetMapping("/user/dashboard")
    public String adminPage() {
        return "userDashboard";
    }
}
