package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.List;

@Service
public class UserService {

    private UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    public Collection<User> getAll() {
        return userStorage.getAll();
    }

    public User delete(User user) {
        return userStorage.delete(user);
    }


    public User addFriend(User user, User userToAdd) {

        if (!isUserValid(user) || !isUserValid(userToAdd)) {
            throw new ConditionsNotMetException("У пользователя id должен быть положительным числом");
        }

        user.getFriends().add(userToAdd.getId());
        userToAdd.getFriends().add(user.getId());

        return userToAdd;
    }

    public User deleteFriend(User user, User userToRemove) {
        if (!isUserValid(user) || !isUserValid(userToRemove)) {
            throw new ConditionsNotMetException("У пользователя id должен быть положительным числом");
        }

        user.getFriends().remove(userToRemove.getId());
        userToRemove.getFriends().remove(user.getId());

        return userToRemove;
    }


    public User addUserFriendById(Integer userId, Integer friendId) {

        if (userId < 0 || friendId < 0) {
            throw new ConditionsNotMetException("У пользователей id должен быть положительным");
        }

        addFriend(userStorage.getUserById(userId), userStorage.getUserById(friendId));
        return userStorage.getUserById(friendId);
    }












    public Collection<Integer> getUserFriends(User user) {
        if (!isUserValid(user)) {
            throw new ConditionsNotMetException("У пользователя id должен быть положительным числом");
        }
        return user.getFriends();
    }


    public boolean isUserValid(User user) {
        return user.getId() > 0;
    }
}
