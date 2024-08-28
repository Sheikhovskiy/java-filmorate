package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmRepository {

    public Film create(Film film);
    Optional<Film> getFilmById(int id);

    Film save(Film film);

    Film update(Film film);

    List<Film> getAll();

    void deleteById(int id);

    void addLike(int filmId, int userId);

    void removeLike(int filmId, int userId);

    List<Film> getMostPopularFilms(int count);
}
