package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {

    public User create(User user);

    public User update(User newUser);

    public Collection<User> getAll();

    public User delete(User user);

    public Optional<User> getUserById(Integer id);

}
