package com.samuelangan.mycompagny.authentification.web.controller;

import com.samuelangan.mycompagny.authentification.domain.User;
import com.samuelangan.mycompagny.authentification.service.MailService;
import com.samuelangan.mycompagny.authentification.service.UserService;
import com.samuelangan.mycompagny.authentification.service.dto.AdminUserDTO;
import com.samuelangan.mycompagny.authentification.service.mapper.UserMapper;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class AuthPageController {

    private final UserService userService;
    private final MailService mailService;
    private final UserMapper userMapper;
    public AuthPageController(UserService userService, MailService mailService, UserMapper userMapper) {
        this.userService = userService;
        this.mailService = mailService;
        this.userMapper = userMapper;
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
            User user = userMapper.userDTOToUser(userDTO);
            user.setPassword(password);
            userService.registerUser(userDTO, password);

            mailService.sendCreationEmail(user);

            model.addAttribute("message", "Inscription réussie ! Vous pouvez maintenant vous connecter.");
            return "loginTemplate";
        } catch (Exception e) {
            model.addAttribute("message", "Erreur lors de l'inscription : " + e.getMessage());
            return "registerTemplate";
        }
    }


    /**
     * Activer le compte de l'utilisateur.
     */
    @GetMapping("/activate")
    public String activateAccount(@RequestParam(value = "key") String key, Model model) {
        Optional<User> user = userService.activateRegistration(key);
        if (!user.isPresent()) {
            model.addAttribute("error", "Invalid activation key.");
            return "error";
        }

        mailService.sendActivationConfirmEmail(user.get());
        model.addAttribute("message", "Your account has been activated.");
        return "accountActivated";
    }

    /**
     * Afficher le profil de l'utilisateur connecté.
     */
    @GetMapping("/profile")
    public String viewProfile(Model model) {
        User user = userService.getUserWithAuthorities()
                .orElseThrow(() -> new RuntimeException("User could not be found"));
        model.addAttribute("user", user);
        return "profile";  // Vue Thymeleaf pour afficher le profil
    }


    @GetMapping("/access-denied")
    public String accessDenied() {
        return "accessDenied"; // fichier accessDenied.html dans templates
    }

}
