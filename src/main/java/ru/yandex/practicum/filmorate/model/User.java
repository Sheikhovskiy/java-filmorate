package ru.yandex.practicum.filmorate.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;


@Data
@Valid
@NoArgsConstructor
public class User {

    private Integer id;

    @NonNull
    @NotEmpty
    @Email
    private String email;

    @NonNull
    @NotBlank
    private String login;

    private String name;

    @NonNull
    @PastOrPresent
    private LocalDate birthday;


}
