//package ru.yandex.practicum.filmorate.controller;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import ru.yandex.practicum.filmorate.FilmorateApplication;
//import ru.yandex.practicum.filmorate.model.Film;
//import ru.yandex.practicum.filmorate.service.FilmService;
//import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
//import ru.yandex.practicum.filmorate.storage.UserDbStorage;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.context.ContextConfiguration;
//
//import java.time.LocalDate;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@ContextConfiguration(classes = FilmorateApplication.class)
//class FilmControllerTest {
//
//    private FilmController filmController;
//    private FilmService filmService;
//
//    @Autowired
//    private FilmDbStorage filmDbStorage;
//
//    @Autowired
//    private UserDbStorage userDbStorage;
//
//    @BeforeEach
//    void setUp() {
//        filmService = new FilmService(filmDbStorage, userDbStorage);
//        filmController = new FilmController(filmService);
//    }
//
//    @Test
//    void createFilm() {
//        Film film = new Film();
//        film.setName("Фильм 1");
//        film.setDescription("Описание фильма 2");
//        film.setReleaseDate(LocalDate.of(2010, 7, 16));
//        film.setDuration(148);
//
//        Film createdFilm = filmController.create(film);
//
//        assertNotNull(createdFilm.getId(), "ID фильма должен существовать и быть числом");
//        assertEquals("Фильм 1", createdFilm.getName(), "Название фильма должно совпадать с тем, что мы задали");
//        assertEquals("Описание фильма 2", createdFilm.getDescription(), "Описание фильма должно совпадать с тем, что мы задали");
//        assertEquals(LocalDate.of(2010, 7, 16), createdFilm.getReleaseDate(), "Дата выпуска фильма должна быть той, что мы задали");
//        assertEquals(148, createdFilm.getDuration(), "Длительность фильма должна быть 148 минут");
//    }
//}
