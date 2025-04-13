package com.samuelangan.mycompagny.authentification.service.dto;


import com.samuelangan.mycompagny.authentification.config.Constants;
import com.samuelangan.mycompagny.authentification.domain.Authority;
import com.samuelangan.mycompagny.authentification.domain.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * A DTO representing a user, with his authorities.
 */

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class AdminUserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotBlank
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    private String login;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @Email
    @Size(min = 5, max = 254)
    private String email;

    private boolean activated = false;

    private Set<String> authorities;

    public AdminUserDTO(User user) {
        this.id = user.getId();
        this.login = user.getLogin();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.activated = user.isActivated();
        this.authorities = user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toSet());
    }

}

