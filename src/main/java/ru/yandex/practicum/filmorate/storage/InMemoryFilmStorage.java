package ru.yandex.practicum.filmorate.storage;

import jakarta.validation.Valid;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> films = new HashMap<>();

    private Integer currentMaxFilmId = 0;

    private static final LocalDate MINIMAL_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    @Override
    public Film create(Film film) {
        if (isValid(film)) {

            film.setId(getNextId());
            films.put(film.getId(), film);
            return film;
        }
        return film;
    }

    @Override
    public Film update(Film newFilm) {
        if (isValid(newFilm)) {


            if (newFilm.getId() == null) {

                throw new ConditionsNotMetException("Id фильма должен быть указан !");
            } else if (!films.containsKey(newFilm.getId())) {
                throw new NotFoundException("Фильм с id " + newFilm.getId() + " не существует !");
            }

            Film oldFilm = films.get(newFilm.getId());

            oldFilm.setName(newFilm.getName());
            oldFilm.setDescription(newFilm.getDescription());
            oldFilm.setDuration(newFilm.getDuration());
            oldFilm.setReleaseDate(newFilm.getReleaseDate());

            return oldFilm;

        }
        return newFilm;
    }

    @Override
    public Collection<Film> getAll() {
        return films.values();
    }

    @Override
    public Film delete(Film film) {
        if (film.getId() == null || film.getId() < 1) {
            throw new ConditionsNotMetException("У фильма должен быть положительный id");
        }
        films.remove(film.getId());

        return film;
    }

    @Override
    public Optional<Film> getFilmById(Integer id) {
        if (id < 1) {
            throw new ConditionsNotMetException("У фильма должен быть положительный id");
        }
        if (!films.containsKey(id)) {
            return Optional.empty();
        }
        return Optional.of(films.get(id));
    }




    public boolean isValid(@Valid Film film) {

        if (film.getReleaseDate().isBefore(MINIMAL_RELEASE_DATE)) {
            throw new ConditionsNotMetException("Дата релиза фильма должна быть не раньше 28 декабря 1895 года !");
        }
        return true;

    }


    private Integer getNextId() {
        currentMaxFilmId += 1;
        return currentMaxFilmId;
    }


}
