package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

//    @Autowired
//    public UserController(UserService userService) {
//        this.userService = userService;
//    }



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


    @PutMapping("/{id}/friends/{friendId}")
    public User addUserFriendById(@PathVariable Integer id, @PathVariable Integer friendId) {
        return userService.addUserFriendById(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteUserFriendById(@PathVariable Integer id, @PathVariable Integer friendId) {
        return userService.deleteUserFriendById(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getAllFriendsByUserId(@PathVariable Integer id) {
        return userService.getAllFriendsByUserId(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriendsOfTwoUsers(@PathVariable Integer id, @PathVariable Integer otherId) {
        return userService.getCommonFriendsOfTwoUsers(id, otherId);
    }




}
