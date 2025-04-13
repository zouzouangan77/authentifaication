package com.samuelangan.mycompagny.authentification.web.controller;
import com.samuelangan.mycompagny.authentification.security.AuthoritiesConstants;
import com.samuelangan.mycompagny.authentification.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.samuelangan.mycompagny.authentification.service.dto.AdminUserDTO;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
@Controller
@RequestMapping("/admin")
public class AdminPageController {

    private final UserService userService;


    public AdminPageController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/dashboard")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public String showDashboard(Model model) {
        model.addAttribute("users", userService.getAllAdminUsers());
        return "adminDashboard";
    }

    @PostMapping("/toggle")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public String toggleUserActivation(@RequestParam Long userId) {
        userService.toggleActivation(userId);
        return "redirect:/admin/dashboard";
    }
}