package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.JdbcGenreRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class GenreDbStorage implements GenreStorage {

    private final JdbcGenreRepository jdbcGenreRepository;

    @Override
    public List<Genre> getAll() {
        return jdbcGenreRepository.getAll();
    }

    @Override
    public Optional<Genre> getById(int id) {
        return jdbcGenreRepository.getById(id);
    }

    @Override
    public List<Genre> getGenresByFilmId(int filmId) {
        return jdbcGenreRepository.getGenresByFilmId(filmId);
    }

    @Override
    public void load(List<Film> films) {
        jdbcGenreRepository.load(films);

    }
}
