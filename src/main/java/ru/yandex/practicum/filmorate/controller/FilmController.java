package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.format.DateTimeFormatters;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Integer, Film> films = new HashMap<>();

    private final static Logger log = LoggerFactory.getLogger(FilmController.class);

    @PostMapping
    public Film create(@RequestBody Film film) {

        if (isValid(film)) {

//            if (film.getId() != null && films.containsKey(film.getId())) {
//                throw new DuplicatedDataException("Фильм с id " + film.getId() + " уже существует !");
//            }

            film.setId(getNextId());
            films.put(getNextId(), film);
            return film;
        }
        return film;
    }

    private Integer getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return Math.toIntExact(++currentMaxId);
    }


    @PutMapping
    public Film update(@RequestBody Film newFilm) {

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
        System.out.println("Произошла ошибка при обновлении фильма, он не валидный");
        return newFilm;
    }

    @GetMapping
    public Collection<Film> getAll() {
        return films.values();
    }



    public boolean isValid(Film film) {
        final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        final LocalDate minimalReleaseDate = LocalDate.parse("1895-12-28", dateTimeFormatter);

        if (film.getName() == null ) {
            throw new ConditionsNotMetException("Название не может быть null");
        } else if (film.getName() == "") {
            throw new ConditionsNotMetException("Название не может быть пустым");
        } else if (film.getDescription().length() > 200) {
            throw new ConditionsNotMetException("Название фильма не может быть длиннее 200 символов !");
        } else if (film.getReleaseDate().isBefore(minimalReleaseDate)) {
            throw new ConditionsNotMetException("Дата релиза фильма должна быть не раньше 28 декабря 1895 года !");
        } else if (!(film.getDuration() instanceof Integer) || !(film.getDuration() > 0)) {
            throw new ConditionsNotMetException("Длительность фильма должна быть целым положительным числом !");
        }
        return true;


    }





}
