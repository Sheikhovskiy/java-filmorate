package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class FilmService  {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(@Qualifier("FilmDbStorage") FilmStorage filmStorage, @Qualifier("UserDbStorage") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public Collection<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film delete(Film film) {
        return filmStorage.delete(film);
    }

    public Film addLike(User user, Film filmToLike) {
        if (!isUserValid(user)) {
            throw new ConditionsNotMetException("У пользователя id должен быть положительным числом");
        }
        if (!isFilmValid(filmToLike)) {
            throw new ConditionsNotMetException("У фильма id должен быть положительным числом");
        }

        filmToLike.getLikes().add(user.getId());
        filmStorage.update(filmToLike);
        return filmToLike;
    }

    public Film deleteLike(User user, Film filmToUnlike) {
        if (!isUserValid(user)) {
            throw new ConditionsNotMetException("У пользователя id должен быть положительным числом");
        }
        if (!isFilmValid(filmToUnlike)) {
            throw new ConditionsNotMetException("У фильма id должен быть положительным числом");
        }

        filmToUnlike.getLikes().remove(user.getId());
        filmStorage.update(filmToUnlike);
        return filmToUnlike;
    }



    public Collection<Film> getTenMostPopularFilms() {

        List<Film> listOfFilms = (List<Film>) filmStorage.getAll();

        listOfFilms.sort(new FilmLikeRankingComparator().reversed());

        return listOfFilms.stream()
                .limit(10)
                .collect(Collectors.toList());
    }


    public boolean isFilmValid(Film film) {
        return film.getId() > 0;
    }

    public boolean isUserValid(User user) {
        return user.getId() > 0;
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

    public class FilmLikeRankingComparator implements Comparator<Film> {

        @Override
        public int compare(Film firstFilm, Film secondFilm) {
            return Integer.compare(firstFilm.getLikes().size(), secondFilm.getLikes().size());
        }
    }


    public Film getFilmById(Integer filmId) {
        if (filmStorage.getFilmById(filmId).isEmpty()) {
            throw new NotFoundException("Фильма с таким идентификатором id " + filmId + " не существует!");
        }
        return filmStorage.getFilmById(filmId).get();
    }


}
