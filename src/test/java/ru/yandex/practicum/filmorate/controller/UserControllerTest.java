package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


class UserControllerTest {

    private UserController userController;

    private User user;

    @BeforeEach
    void setUp() {
        userController = new UserController();
    }

    @Test
    void createUser() {
        // Arrange
        User user = new User();
        user.setName("Пользователь 1");
        user.setEmail("test@example.com");
        user.setLogin("ЛогинТест");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        // Act
        User createdUser = userController.create(user);

        // Assert
        assertNotNull(createdUser.getId(), "ID пользователя должно существовать ");
        assertEquals("Пользователь 1", createdUser.getName(), "Имя пользователя должен быть тем, что мы задали");
        assertEquals("test@example.com", createdUser.getEmail(), "Почта пользователя должен быть тем, что мы задали");
        assertEquals("ЛогинТест", createdUser.getLogin(), "Логин пользователя должен быть тем, что мы задали");
        assertEquals(LocalDate.of(1990, 1, 1), createdUser.getBirthday(), "Дата рождения пользователя должен быть той, что мы задали'");
    }






}