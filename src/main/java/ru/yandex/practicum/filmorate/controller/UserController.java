package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return userService.create(user);
    }


    @PutMapping
    public User update(@Valid @RequestBody User newUser) {
        return userService.update(newUser);
    }


    @GetMapping
    public Collection<User> getAll() {
        return userService.getAll();
    }

    @DeleteMapping
    public User delete(@Valid @RequestBody User user) {
        return userService.delete(user);
    }


    @PutMapping("/users/{id}/friends/{friendId}")
    public User addUserFriendById(@PathVariable Integer id, @PathVariable Integer friendsId) {
        return userService.addUserFriendById(id, friendsId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public User deleteUserFriendById(@PathVariable Integer id, @PathVariable Integer friendsId) {

    }

    @GetMapping("/users/{id}/friends")
    public Collection<User> getAllFriendsByUserId(@PathVariable Integer id) {

    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriendsOfTwoUsers(@PathVariable Integer id, @PathVariable Integer otherId) {

    }





}
