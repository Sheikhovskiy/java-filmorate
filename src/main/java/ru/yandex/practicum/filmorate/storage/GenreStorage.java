package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreStorage {

    List<Genre> getAll();

    Optional<Genre> getById(int id);

    List<Genre> getGenresByFilmId(int filmId);

    void load(List<Film> films);

}
