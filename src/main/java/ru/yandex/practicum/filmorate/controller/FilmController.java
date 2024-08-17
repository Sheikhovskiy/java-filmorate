package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @Autowired
    FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film newFilm) {
        return filmService.update(newFilm);
    }

    @GetMapping
    public Collection<Film> getAll() {
        return filmService.getAll();
    }

    @DeleteMapping
    public Film delete(@Valid @RequestBody Film film) {
        return filmService.delete(film);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public Film likeFilmByUserId(@PathVariable Integer id, @PathVariable Integer userId) {

    }

    @DeleteMapping("/films/{id}/like{userId}")
    public Film unlikeFilmByUserId(@PathVariable Integer id, @PathVariable Integer userId) {

    }

    @GetMapping("/films/popular?count={count}")
    public Collection<Film> getMostPopularFilms(@RequestParam(defaultValue = "10") Integer count) {

    }




}
