package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


class FilmControllerTest {

    private FilmController filmController;

    private InMemoryFilmStorage inMemoryFilmStorage;

    private FilmService filmService;


    private Film film;

    @BeforeEach
    void setUp() {
        filmController = new FilmController(filmService, inMemoryFilmStorage);
    }

    @Test
    void createFilm() {
        // Arrange
        Film film = new Film();
        film.setName("Фильм 1");
        film.setDescription("Описание фильма 2");
        film.setReleaseDate(LocalDate.of(2010, 7, 16));
        film.setDuration(148);

        // Act
        Film createdFilm = filmController.create(film);

        // Assert
        assertNotNull(createdFilm.getId(), "ID фильма должен существовать и быть числом");
        assertEquals("Фильм 1", createdFilm.getName(), "Название фильма должно совпадать с тем, что мы задали");
        assertEquals("Описание фильма 2", createdFilm.getDescription(), "Описание фильма должно совпадать с тем, что мы задали");
        assertEquals(LocalDate.of(2010, 7, 16), createdFilm.getReleaseDate(), "Дата выпуска фильма должна быть той, что мы задали");
        assertEquals(148, createdFilm.getDuration(), "Длительность фильма должна быть 148 минут");
    }





}