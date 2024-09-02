package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;
import ru.yandex.practicum.filmorate.FilmorateApplication;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.JdbcMpaRepository;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = FilmorateApplication.class)
public class FilmServiceTest {

    @Autowired
    private FilmService filmService;

    @Autowired
    private FilmStorage filmStorage;

    @Autowired
    private UserStorage userStorage;

    @Autowired
    private GenreStorage genreStorage;

    @Autowired
    private JdbcMpaRepository jdbcMpaRepository;

    private Film testFilm;
    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setEmail("user@example.com");
        testUser.setLogin("userlogin");
        testUser.setName("User Name");
        testUser.setBirthday(LocalDate.of(1985, 5, 5));
        userStorage.create(testUser);

        testFilm = new Film();
        testFilm.setName("Test Film");
        testFilm.setDescription("Test Description");
        testFilm.setReleaseDate(LocalDate.of(2000, 1, 1));
        testFilm.setDuration(120);
        testFilm.setMpa(new Mpa(1, "G"));
        testFilm.setGenres(new LinkedHashSet<>(Collections.singletonList(new Genre(1, "Comedy"))));
        filmStorage.create(testFilm);
    }

    @Test
    void testCreateFilm() {
        Film newFilm = new Film();
        newFilm.setName("New Film");
        newFilm.setDescription("New Description");
        newFilm.setReleaseDate(LocalDate.of(2020, 1, 1));
        newFilm.setDuration(90);
        newFilm.setMpa(new Mpa(1, "G"));
        newFilm.setGenres(new LinkedHashSet<>(Collections.singletonList(new Genre(1, "Comedy"))));

        Film createdFilm = filmService.create(newFilm);

        assertThat(createdFilm).isNotNull();
        assertThat(createdFilm.getName()).isEqualTo("New Film");
        assertThat(filmStorage.getFilmById(createdFilm.getId())).isPresent();
    }


    @Test
    void testUpdateFilm() {
        testFilm.setName("Updated Film");
        Film updatedFilm = filmService.update(testFilm);

        assertThat(updatedFilm.getName()).isEqualTo("Updated Film");
        assertThat(filmStorage.getFilmById(testFilm.getId()))
                .isPresent()
                .hasValueSatisfying(film -> assertThat(film.getName()).isEqualTo("Updated Film"));
    }


    @Test
    void testLikeFilmByUserId() {
        Optional<Film> likedFilm = filmService.likeFilmByUserId(testFilm.getId(), testUser.getId());

        assertThat(likedFilm).isPresent();
        assertThat(likedFilm.get().getName()).isEqualTo("Test Film");
    }

    @Test
    void testUnlikeFilmByUserId() {
        filmService.likeFilmByUserId(testFilm.getId(), testUser.getId());
        Optional<Film> unlikedFilm = filmService.unlikeFilmByUserId(testFilm.getId(), testUser.getId());

        assertThat(unlikedFilm).isPresent();
        assertThat(unlikedFilm.get().getName()).isEqualTo("Test Film");
    }

    @Test
    void testValidateFilmData() {
        boolean isValid = filmService.validateFilmData(testFilm);

        assertThat(isValid).isTrue();
    }

    @Test
    void testValidateFilmDataInvalidDate() {
        testFilm.setReleaseDate(LocalDate.of(1800, 1, 1));

        assertThatThrownBy(() -> filmService.validateFilmData(testFilm))
                .isInstanceOf(ConditionsNotMetException.class)
                .hasMessageContaining("Дата релиза должна быть указана и не может быть ранее 28 декабря 1895 года!");
    }

    @Test
    void testValidateFilmDataInvalidMpa() {
        Film newFilm = new Film();
        newFilm.setName("Invalid MPA Film");
        newFilm.setDescription("Test Description");
        newFilm.setReleaseDate(LocalDate.of(2020, 1, 1));
        newFilm.setDuration(90);
        newFilm.setMpa(new Mpa(999, "Invalid")); // Невалидный MPA

        assertThatThrownBy(() -> filmService.validateFilmData(newFilm))
                .isInstanceOf(ConditionsNotMetException.class)
                .hasMessageContaining("MPA-рейтинг должен быть указан и существовать в системе!");
    }



}
