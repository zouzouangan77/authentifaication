package com.samuelangan.mycompagny.authentification.web.controller;
import com.samuelangan.mycompagny.authentification.domain.User;
import com.samuelangan.mycompagny.authentification.security.AuthoritiesConstants;
import com.samuelangan.mycompagny.authentification.service.MailService;
import com.samuelangan.mycompagny.authentification.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminPageController {

    private final UserService userService;
    private final MailService mailService;


    public AdminPageController(UserService userService, MailService mailService) {
        this.userService = userService;
        this.mailService = mailService;
    }


    @GetMapping("/dashboard")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public String showDashboard(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "adminDashboard";
    }

    @GetMapping("/activate")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public String activateAccountAdmin(@RequestParam("key") String key, RedirectAttributes redirectAttributes) {
        Optional<User> user = userService.activateRegistration(key);
        if (user.isPresent()) {
            mailService.sendActivationConfirmEmail(user.get());
            redirectAttributes.addFlashAttribute("message", "Compte activé avec succès.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Clé d’activation invalide.");
        }
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/toggle")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public String toggleUserActivation(@RequestParam Long userId, RedirectAttributes redirectAttributes) {
        boolean success = userService.toggleActivation(userId);
        if (success) {
            redirectAttributes.addFlashAttribute("message", "Le statut du compte a été modifié.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Utilisateur introuvable.");
        }
        return "redirect:/admin/dashboard";
    }


}