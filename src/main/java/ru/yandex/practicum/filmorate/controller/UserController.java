package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;


import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final HashMap<Integer, User> users = new HashMap<>();

    private Integer currentMaxUserId = 0;

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        if (isValid(user)) {

            user.setId(getNextId());
            users.put(user.getId(), user);
            return user;
        }
        System.out.println(user);
        return user;
    }

    private Integer getNextId() {
        currentMaxUserId++;
        return currentMaxUserId;
    }



    @PutMapping
    public User update(@Valid @RequestBody User newUser) {
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


    public boolean isValid(@Valid User user) {

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return true;

    }

}
