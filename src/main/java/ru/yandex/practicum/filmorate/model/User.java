package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @NotNull
    private long id;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String login;
    private String name;

    @PastOrPresent
    private LocalDate birthday;
}
