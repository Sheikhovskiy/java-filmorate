package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.JdbcFilmRepository;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Component
@Qualifier("FilmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private final JdbcFilmRepository jdbcFilmRepository;

    @Autowired
    public FilmDbStorage(JdbcFilmRepository jdbcFilmRepository) {
        this.jdbcFilmRepository = jdbcFilmRepository;
    }


    public Film create(Film film) {
        System.out.println("Попытка создания фильма: " + film);

        validateFilmData(film);

        Optional<Film> alreadyExistFilm = jdbcFilmRepository.getFilmByNameAndReleaseDate(film.getName(), film.getReleaseDate());


        if (alreadyExistFilm.isPresent()) {
            throw new DuplicatedDataException("Фильм с таким названием и датой релиза уже существует!");
        }

        Film createdFilm = jdbcFilmRepository.create(film);
        System.out.println("Фильм успешно создан: " + createdFilm);

        return createdFilm;
    }

    @Override
    public Film update(Film newFilm) {
        validateFilmData(newFilm);

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
        validateFilmData(film);

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


    public boolean validateFilmData(Film film) {
        if (film.getName() == null || film.getName().isEmpty()) {
            throw new ConditionsNotMetException("Фильм должен иметь название!");
        }

        if (film.getDescription() == null || film.getDescription().isEmpty()) {
            throw new ConditionsNotMetException("Фильм должен иметь описание!");
        }

        if (film.getReleaseDate() == null) {
            throw new ConditionsNotMetException("Дата релиза должна быть указана!");
        }

        if (film.getDuration() <= 0) {
            throw new ConditionsNotMetException("Продолжительность фильма должна быть положительным числом!");
        }
        return true;
    }



    public boolean isUserValid(User user) {
        return user.getId() > 0;
    }

}























