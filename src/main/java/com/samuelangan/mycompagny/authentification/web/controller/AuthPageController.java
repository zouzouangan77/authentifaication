package com.samuelangan.mycompagny.authentification.web.controller;

import com.samuelangan.mycompagny.authentification.service.UserService;
import com.samuelangan.mycompagny.authentification.service.dto.AdminUserDTO;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthPageController {

    private final UserService userService;

    public AuthPageController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "loginTemplate";
    }


    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("userDTO", new AdminUserDTO());
        return "registerTemplate";
    }

    @PostMapping("/register")
    public String registerUser(
            @Valid @ModelAttribute("userDTO") AdminUserDTO userDTO,
            BindingResult bindingResult,
            @RequestParam("password") String password,
            Model model
    ) {
        // Gestion des erreurs de validation
        if (bindingResult.hasErrors()) {
            return "registerTemplate";
        }

        // Vérifie que le mot de passe n’est pas vide
        if (password == null || password.trim().isEmpty()) {
            model.addAttribute("message", "Le mot de passe est requis.");
            return "registerTemplate";
        }

        try {
            userService.registerUser(userDTO, password);
            model.addAttribute("message", "Inscription réussie ! Vous pouvez maintenant vous connecter.");
            return "loginTemplate";
        } catch (Exception e) {
            model.addAttribute("message", "Erreur lors de l'inscription : " + e.getMessage());
            return "registerTemplate";
        }
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "accessDenied"; // fichier accessDenied.html dans templates
    }
}
