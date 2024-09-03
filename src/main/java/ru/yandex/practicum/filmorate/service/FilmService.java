package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.MpaRepository;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class FilmService  {

    private final FilmStorage filmStorage;

    private final UserStorage userStorage;

    private final GenreStorage genreStorage;

    private final MpaRepository mpaRepository;

    private static final LocalDate MINIMAL_RELEASE_DATE = LocalDate.of(1895, 12, 28);


    public Film create(Film film) {
        validateFilmData(film);

        return filmStorage.create(film);
    }

    public Film update(Film film) {
        validateFilmData(film);

        return filmStorage.update(film);
    }

    public Collection<Film> getAll() {
        List<Film> films = (List<Film>) filmStorage.getAll();
        genreStorage.load(films);

        return films;
    }

    public Film delete(Film film) {
        validateFilmData(film);

        return filmStorage.delete(film);
    }


    public Collection<Film> getMostPopularFilms(Integer size) {

        return ((FilmDbStorage) filmStorage).getMostPopularFilms(size);
    }

    public Film getFilmById(Integer filmId) {
        if (filmStorage.getFilmById(filmId).isEmpty()) {
            throw new NotFoundException("Фильма с таким идентификатором id " + filmId + " не существует!");
        }
        Film film = filmStorage.getFilmById(filmId).get();
        genreStorage.load(List.of(film));

        return film;
    }


    public Optional<Film> likeFilmByUserId(Integer filmId, Integer userId) {
        Optional<User> userOptional = userStorage.getUserById(userId);
        Optional<Film> filmOptional = filmStorage.getFilmById(filmId);

        if (userOptional.isPresent() && filmOptional.isPresent()) {
            ((FilmDbStorage) filmStorage).addLike(filmId, userId);

            return filmStorage.getFilmById(filmId);
        } else {
            return Optional.empty();
        }
    }

    public Optional<Film> unlikeFilmByUserId(Integer filmId, Integer userId) {
        Optional<User> userOptional = userStorage.getUserById(userId);
        Optional<Film> filmOptional = filmStorage.getFilmById(filmId);

        if (userOptional.isPresent() && filmOptional.isPresent()) {
            ((FilmDbStorage) filmStorage).removeLike(filmId, userId);
            return filmStorage.getFilmById(filmId);
        } else {
            return Optional.empty();
        }
    }


    public boolean validateFilmData(Film film) {
        if (film.getReleaseDate().isBefore(MINIMAL_RELEASE_DATE)) {
            throw new ConditionsNotMetException("Дата релиза должна быть указана и не может быть ранее 28 декабря 1895 года!");
        }

        if (mpaRepository.getById(film.getMpa().getId()).isEmpty()) {
            throw new ConditionsNotMetException("MPA-рейтинг должен быть указан и существовать в системе!");
        }


        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                if (genreStorage.getById((int) genre.getId()).isEmpty()) {
                    throw new ConditionsNotMetException("Жанр с ID " + genre.getId() + " не найден!");
                }
            }
        }
        return true;
    }
}
