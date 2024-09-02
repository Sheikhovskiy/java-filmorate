package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;
import ru.yandex.practicum.filmorate.FilmorateApplication;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = FilmorateApplication.class)
public class UserServiceTest {



    @Autowired
    private final UserDbStorage userStorage;

    private User createdUser;


    @BeforeEach
    void setUp() {
        User newUser = new User();
        newUser.setEmail("delete@example.com");
        newUser.setLogin("deleteuser");
        newUser.setName("Delete User");
        newUser.setBirthday(LocalDate.of(1990, 1, 1));

        createdUser = userStorage.create(newUser);

    }

    @Test
    public void testCreateUser() {
        User newUser = new User();
        newUser.setEmail("test@example.com");
        newUser.setLogin("testuser");
        newUser.setName("Test User");
        newUser.setBirthday(LocalDate.of(1990, 1, 1));

        User createdUser = userStorage.create(newUser);

        assertThat(createdUser).isNotNull();
        assertThat(createdUser.getId()).isNotNull();
        assertThat(createdUser.getEmail()).isEqualTo("test@example.com");
        assertThat(createdUser.getLogin()).isEqualTo("testuser");
    }


    @Test
    public void testUpdateUser() {
        createdUser.setName("Updated Name");

        User updatedUser = userStorage.update(createdUser);

        assertThat(updatedUser.getName()).isEqualTo("Updated Name");
    }

    @Test
    public void testDeleteUser() {
//		User newUser = new User();
//		newUser.setEmail("delete@example.com");
//		newUser.setLogin("deleteuser");
//		newUser.setName("Delete User");
//		newUser.setBirthday(LocalDate.of(1990, 1, 1));
//
//		User createdUser = userStorage.create(newUser);
        userStorage.delete(createdUser);

        Optional<User> deletedUser = userStorage.getUserById(createdUser.getId());
        assertThat(deletedUser).isNotPresent();
    }

    @Test
    public void testFindAllUsers() {
        assertThat(userStorage.getAll()).isNotEmpty();
    }

}
