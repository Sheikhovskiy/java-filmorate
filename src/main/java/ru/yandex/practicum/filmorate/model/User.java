package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;


@Data
public class User {

    Integer id;

    String email;

    String login;

    String name;

    LocalDate birthday;


}
