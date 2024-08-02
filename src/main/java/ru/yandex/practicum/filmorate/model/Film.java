package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;

/**
 * Film.
 */
@Data
public class Film {

    Integer id;

    String name;

    String description;

    LocalDate releaseDate;

    Integer duration;

}