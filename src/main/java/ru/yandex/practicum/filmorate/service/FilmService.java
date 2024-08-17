package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
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

    public Film delete(Film fIlm) {
        return filmStorage.delete(fIlm);
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

    public Film deleteFriend(User user, Film filmToUnlike) {
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

//        listOfFilms.sort(Comparator.comparing(film -> film.getLikes().size()).reversed());

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



    public class FilmLikeRankingComparator implements Comparator<Film> {

        @Override
        public int compare(Film firstFilm, Film secondFilm) {
            return Integer.compare(firstFilm.getLikes().size(), secondFilm.getLikes().size());
        }
    }


}
