package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {

    @Autowired
    private final FilmStorage filmStorage;

    @Autowired
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
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


    public Film likeFilmByUserId(Integer filmId, Integer userId) {
        return addLike(userStorage.getUserById(userId), filmStorage.getFilmById(filmId));
    }

    public Film unlikeFilmByUserId(Integer filmId, Integer userId) {
        return deleteLike(userStorage.getUserById(userId), filmStorage.getFilmById(filmId));
    }

    public Collection<Film> getMostPopularFilms(Integer size) {

        List<Film> listOfFilms = new ArrayList<>(filmStorage.getAll());

        listOfFilms.sort(new FilmLikeRankingComparator().reversed());

        return listOfFilms.stream()
                .limit(size)
                .toList();
    }


    public class FilmLikeRankingComparator implements Comparator<Film> {

        @Override
        public int compare(Film firstFilm, Film secondFilm) {
            return Integer.compare(firstFilm.getLikes().size(), secondFilm.getLikes().size());
        }
    }


}
