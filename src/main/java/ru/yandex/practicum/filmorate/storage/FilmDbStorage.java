package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.JdbcFilmRepository;
import ru.yandex.practicum.filmorate.repository.JdbcGenreRepository;
import ru.yandex.practicum.filmorate.repository.JdbcMpaRepository;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Component
public class FilmDbStorage implements FilmStorage {

    private final JdbcFilmRepository jdbcFilmRepository;

    public FilmDbStorage(JdbcFilmRepository jdbcFilmRepository, JdbcMpaRepository jdbcMpaRepository, JdbcGenreRepository jdbcGenreRepository) {
        this.jdbcFilmRepository = jdbcFilmRepository;
    }

    public Film create(Film film) {
        System.out.println("Попытка создания фильма: " + film);

        Film createdFilm = jdbcFilmRepository.create(film);
        System.out.println("Фильм успешно создан: " + createdFilm);

        return createdFilm;
    }

    @Override
    public Film update(Film newFilm) {

        Optional<Film> alreadyExistFilm = jdbcFilmRepository.getFilmById(newFilm.getId());

        if (alreadyExistFilm.isEmpty()) {
            throw new NotFoundException("Такого фильма не существует!");
        }
        return jdbcFilmRepository.update(newFilm);
    }

    public void addLike(int filmId, int userId) {
        jdbcFilmRepository.addLike(filmId, userId);
    }

    // Новый метод для удаления лайка
    public void removeLike(int filmId, int userId) {
        jdbcFilmRepository.removeLike(filmId, userId);
    }

    public Collection<Film> getMostPopularFilms(int count) {
        return jdbcFilmRepository.getMostPopularFilms(count);
    }

    @Override
    public Collection<Film> getAll() {
        return jdbcFilmRepository.getAll();
    }

    @Override
    public Film delete(Film film) {

        jdbcFilmRepository.deleteById(film.getId());
        return film;
    }

    @Override
    public Optional<Film> getFilmById(Integer id) {
        if (id < 1) {
            throw new ConditionsNotMetException("Идентификатор фильма должен быть целым и положительным числом!");
        }
        return jdbcFilmRepository.getFilmById(id);
    }

}























