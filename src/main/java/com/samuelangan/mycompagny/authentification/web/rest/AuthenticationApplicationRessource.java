package com.samuelangan.mycompagny.authentification.web.rest;

import com.samuelangan.mycompagny.authentification.domain.User;
import com.samuelangan.mycompagny.authentification.repository.UserRepository;
import com.samuelangan.mycompagny.authentification.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationApplicationRessource {



}
