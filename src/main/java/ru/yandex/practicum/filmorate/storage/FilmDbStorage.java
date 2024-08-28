package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.JdbcFilmRepository;
import ru.yandex.practicum.filmorate.repository.JdbcGenreRepository;
import ru.yandex.practicum.filmorate.repository.JdbcMpaRepository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

@Slf4j
@Component
@Qualifier("FilmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private final JdbcFilmRepository jdbcFilmRepository;

    private final JdbcMpaRepository jdbcMpaRepository;

    private final JdbcGenreRepository jdbcGenreRepository;

    @Autowired
    public FilmDbStorage(JdbcFilmRepository jdbcFilmRepository, JdbcMpaRepository jdbcMpaRepository, JdbcGenreRepository jdbcGenreRepository) {
        this.jdbcFilmRepository = jdbcFilmRepository;
        this.jdbcMpaRepository = jdbcMpaRepository;
        this.jdbcGenreRepository = jdbcGenreRepository;
    }



    public Film create(Film film) {
        System.out.println("Попытка создания фильма: " + film);

        validateFilmData(film);

//        log.info(String.valueOf(film.getGenres()));

//        Optional<Film> alreadyExistFilm = jdbcFilmRepository.getFilmByNameAndReleaseDate(film.getName(), film.getReleaseDate());
//
//        if (alreadyExistFilm.isPresent()) {
//            throw new DuplicatedDataException("Фильм с таким названием и датой релиза уже существует!");
//        }

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

        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) { // Примерно, если вы хотите ограничить минимальную дату
            throw new ConditionsNotMetException("Дата релиза должна быть указана и не может быть ранее 28 декабря 1895 года!");
        }

        if (film.getDuration() <= 0) {
            throw new ConditionsNotMetException("Продолжительность фильма должна быть положительным числом!");
        }

        if (film.getMpa() == null || jdbcMpaRepository.getById(film.getMpa().getId()).isEmpty()) {
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




    public boolean isUserValid(User user) {
        return user.getId() > 0;
    }

}























