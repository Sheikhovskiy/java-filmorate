package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@RestController
@RequestMapping("/users")
public class UserController {

    private final HashMap<Integer, User> users = new HashMap<>();

    private final Logger log = LoggerFactory.getLogger(UserController.class);

    @PostMapping
    public User create(@RequestBody User user) {
        if (isValid(user)) {

//            if (user.getId()users.containsKey(user.getId())) {
//                throw new DuplicatedDataException("Пользователь с id " + user.getId() + " уже существует !");
//            }
            user.setId(getNextId());
            users.put(getNextId(), user);
            return user;
        }
        System.out.println(user);
        return user;
    }

    private Integer getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return Math.toIntExact(++currentMaxId);
    }



    @PutMapping
    public User update(@RequestBody User newUser) {
        if (isValid(newUser)) {

            if (!users.containsKey(newUser.getId())) {
                throw new NotFoundException("Пользователь с id " + newUser.getId() + " не существует !");

            } else if (newUser.getId() == null) {
                throw new ConditionsNotMetException("Id пользователя должен быть указан !");
            }

            User oldUser = users.get(newUser.getId());

            oldUser.setName(newUser.getName());
            oldUser.setEmail(newUser.getEmail());
            oldUser.setLogin(newUser.getLogin());
            oldUser.setBirthday(newUser.getBirthday());

            return oldUser;

        }
        return newUser;
    }


    @GetMapping
    public Collection<User> getAll() {
        return users.values();
    }


    public boolean isValid(User user) {

        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new ConditionsNotMetException("Электронная почта не может быть пустой и должна содержать символ @");
        } else if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ConditionsNotMetException("Логин не может быть пустым и содержать пробелы; !");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ConditionsNotMetException("Дата рождения не может быть в будущем !");
        } else if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        return true;

    }

}
