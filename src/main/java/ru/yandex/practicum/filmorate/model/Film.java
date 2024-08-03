package ru.yandex.practicum.filmorate.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;

/**
 * Film.
 */
@Data
@Valid
@NoArgsConstructor
public class Film {

    private Integer id;

    @NonNull
    @NotBlank
    private String name;

    @NonNull
    @Size(max = 200)
    private String description;

    @NonNull
    private LocalDate releaseDate;

    @NonNull
    @Positive
    private Integer duration;


}
