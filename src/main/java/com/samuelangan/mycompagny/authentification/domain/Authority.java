package com.samuelangan.mycompagny.authentification.domain;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.io.Serial;
import java.io.Serializable;

import jakarta.validation.constraints.*;
import lombok.*;
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Getter
public class Authority implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull
    @Size(max = 50)
    @Id
    private String name;
}
