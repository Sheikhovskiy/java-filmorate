package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.Optional;


@RestController
@RequestMapping("/films")
public class FilmController {

    @Autowired
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
    public Film delete(@Valid @RequestBody Film film)  {
        return filmService.delete(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public Optional<Film> likeFilmByUserId(@PathVariable Integer id, @PathVariable Integer userId) {
        return filmService.likeFilmByUserId(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Optional<Film> unlikeFilmByUserId(@PathVariable Integer id, @PathVariable Integer userId) {
        return filmService.unlikeFilmByUserId(id, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> getMostPopularFilms(@RequestParam(defaultValue = "10") Integer count) {
        return filmService.getMostPopularFilms(count);
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable Integer id) {
        return filmService.getFilmById(id);
    }

}
