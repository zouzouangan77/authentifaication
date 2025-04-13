package com.samuelangan.mycompagny.authentification;

import com.samuelangan.mycompagny.authentification.domain.User;
import com.samuelangan.mycompagny.authentification.service.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AuthentificationApplication {
	private final UserService userService;
	@Autowired
	public AuthentificationApplication(UserService userService) {
		this.userService = userService;
	}


	public static void main(String[] args) {
		SpringApplication.run(AuthentificationApplication.class, args);
	}

	@PostConstruct
	public void run() {
		User user = new User();
		user.setLogin("consoleuser");
		user.setPassword("MyStrongPassword123!");
		user.setEmail("console.user@example.com");
		user.setFirstName("Console");
		user.setLastName("User");

		User created = userService.registerUser(user);
		System.out.println("✅ Utilisateur créé avec succès : " + created.getLogin());
	}




}
