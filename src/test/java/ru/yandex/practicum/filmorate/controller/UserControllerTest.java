//package ru.yandex.practicum.filmorate.controller;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
//import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
//import ru.yandex.practicum.filmorate.model.User;
//import ru.yandex.practicum.filmorate.repository.FriendRepository;
//import ru.yandex.practicum.filmorate.repository.JdbcFriendRepository;
//import ru.yandex.practicum.filmorate.repository.JdbcUserRepository;
//import ru.yandex.practicum.filmorate.service.UserService;
//import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
//import ru.yandex.practicum.filmorate.storage.UserDbStorage;
//import ru.yandex.practicum.filmorate.storage.UserStorage;
//
//import java.time.LocalDate;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.mock;
//
//
//class UserControllerTest {
//
//    private UserController userController;
//
//    private UserService userService;
//
//    private NamedParameterJdbcTemplate jdbcTemplate;
//
//    private JdbcUserRepository jdbcUserRepository;
//
//
//    @BeforeEach
//    void setUp() {
//        jdbcTemplate = mock(NamedParameterJdbcTemplate.class);
//        jdbcUserRepository = mock(JdbcUserRepository.class);
//
//        when(jdbcUserRepository.save(any(User.class))).thenReturn(expectedUser);
//
//
//        UserStorage userStorage = new UserDbStorage(jdbcUserRepository);
//        FriendRepository friendRepository = new JdbcFriendRepository(jdbcTemplate);
//
//        userService = new UserService(userStorage, friendRepository);
//        userController = new UserController(userService);
//    }
//
//    @Test
//    void createUser() {
//        User user = new User();
//        user.setName("Пользователь 1");
//        user.setEmail("test@example.com");
//        user.setLogin("ЛогинТест");
//        user.setBirthday(LocalDate.of(1990, 1, 1));
//
//        User createdUser = userController.create(user);
//
//        assertNotNull(createdUser.getId(), "ID пользователя должно существовать ");
//        assertEquals("Пользователь 1", createdUser.getName(), "Имя пользователя должен быть тем, что мы задали");
//        assertEquals("test@example.com", createdUser.getEmail(), "Почта пользователя должен быть тем, что мы задали");
//        assertEquals("ЛогинТест", createdUser.getLogin(), "Логин пользователя должен быть тем, что мы задали");
//        assertEquals(LocalDate.of(1990, 1, 1), createdUser.getBirthday(), "Дата рождения пользователя должен быть той, что мы задали'");
//    }
//
//
//
//
//}