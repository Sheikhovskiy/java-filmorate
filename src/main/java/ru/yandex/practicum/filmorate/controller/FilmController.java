package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.Optional;

@Validated
@RestController
@RequiredArgsConstructor // Вместо конструктора FilmController(FilmService fs) генерирует конструктор для класса, принимающий все final поля и поля с аннотацией @NonNull.
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

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
    public Collection<Film> getMostPopular(@Positive @RequestParam(defaultValue = "10") Integer count) {
        return filmService.getMostPopularFilms(count);
    }

    @GetMapping("/{id}")
    public Film getById(@PathVariable Integer id) {
        return filmService.getFilmById(id);
    }

}
