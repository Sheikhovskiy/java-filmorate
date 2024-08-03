package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Integer, Film> films = new HashMap<>();

    private Integer currentMaxFilmId = 0;

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final LocalDate minimalReleaseDate = LocalDate.parse("1895-12-28", dateTimeFormatter);

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {

        if (isValid(film)) {

            film.setId(getNextId());
            films.put(film.getId(), film);
            return film;
        }
        return film;
    }

    private Integer getNextId() {
        currentMaxFilmId += 1;
        return currentMaxFilmId;
    }


    @PutMapping
    public Film update(@Valid @RequestBody Film newFilm) {

        if (isValid(newFilm)) {

            if (!films.containsKey(newFilm.getId())) {
                throw new NotFoundException("Фильм с id " + newFilm.getId() + " не существует !");

            } else if (newFilm.getId() == null) {
                throw new ConditionsNotMetException("Id фильма должен быть указан !");
            }

            Film oldFilm = films.get(newFilm.getId());

            oldFilm.setName(newFilm.getName());
            oldFilm.setDescription(newFilm.getDescription());
            oldFilm.setDuration(newFilm.getDuration());
            oldFilm.setReleaseDate(newFilm.getReleaseDate());

            return oldFilm;

        }
        return newFilm;
    }

    @GetMapping
    public Collection<Film> getAll() {
        return films.values();
    }



    public boolean isValid(@Valid Film film) {

        if (film.getReleaseDate().isBefore(minimalReleaseDate)) {
            throw new ConditionsNotMetException("Дата релиза фильма должна быть не раньше 28 декабря 1895 года !");
        }
        return true;

    }


}
