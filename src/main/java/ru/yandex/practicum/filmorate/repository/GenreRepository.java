package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreRepository {

    List<Genre> getAll();

    Optional<Genre> getById(int id);

    List<Genre> getGenresByFilmId(int filmId);

    Optional<String> getName(int id);

//    void saveFilmGenres(int filmId, List<Genre> genres);

//    boolean existsById(long id);
}
