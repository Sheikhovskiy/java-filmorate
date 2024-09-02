package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository {

    User create(User user);

    User update(User newUser);

    Collection<User> getAll();

    User delete(User user);

    Optional<User> getUserById(Integer id);



}
