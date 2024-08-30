package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.JdbcGenreRepository;
import ru.yandex.practicum.filmorate.repository.JdbcMpaRepository;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FilmService  {

    private final FilmStorage filmStorage;

    private final UserStorage userStorage;

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final JdbcMpaRepository jdbcMpaRepository;

    private final JdbcGenreRepository jdbcGenreRepository;

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
        return filmStorage.getAll();
    }

    public Film delete(Film film) {
        validateFilmData(film);

        return filmStorage.delete(film);
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
            // Здесь вызываем метод удаления лайка в БД
            ((FilmDbStorage) filmStorage).removeLike(filmId, userId);
            // Возвращаем обновленный фильм
            return filmStorage.getFilmById(filmId);
        } else {
            return Optional.empty();
        }
    }


    public Collection<Film> getMostPopularFilms(Integer size) {
        return ((FilmDbStorage) filmStorage).getMostPopularFilms(size);
    }

    public Film getFilmById(Integer filmId) {
        if (filmStorage.getFilmById(filmId).isEmpty()) {
            throw new NotFoundException("Фильма с таким идентификатором id " + filmId + " не существует!");
        }
        return filmStorage.getFilmById(filmId).get();
    }

    public boolean validateFilmData(Film film) {
        if (film.getReleaseDate().isBefore(MINIMAL_RELEASE_DATE)) { // Примерно, если вы хотите ограничить минимальную дату
            throw new ConditionsNotMetException("Дата релиза должна быть указана и не может быть ранее 28 декабря 1895 года!");
        }

        if (jdbcMpaRepository.getById(film.getMpa().getId()).isEmpty()) {
            throw new ConditionsNotMetException("MPA-рейтинг должен быть указан и существовать в системе!");
        }


        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                if (jdbcGenreRepository.getById((int) genre.getId()).isEmpty()) {
                    throw new ConditionsNotMetException("Жанр с ID " + genre.getId() + " не найден!");
                }
            }
        }

        return true;
    }


    private boolean areAllGenresValid(Film film) {
        if (film.getGenres() == null || film.getGenres().isEmpty()) {
            return true;
        }

        List<Long> genreIds = film.getGenres().stream()
                .map(Genre::getId)
                .collect(Collectors.toList());

        String sql = "SELECT COUNT(*) FROM genres WHERE genre_id IN (:genreIds)";

        MapSqlParameterSource parameters = new MapSqlParameterSource("genreIds", genreIds);

        int count = jdbcTemplate.queryForObject(sql, parameters, Integer.class);

        return count == genreIds.size();
    }


}
