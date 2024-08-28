package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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

        User user = userStorage.getUserById(userId);
        User userFriend = userStorage.getUserById(friendId);

        if (user == null || userFriend == null) {
            throw new NotFoundException("Пользователь(и) не найдены в базе данных");
        }
        addFriend(user, userFriend);
        return userFriend;
    }


    public User deleteUserFriendById(Integer userId, Integer friendId) {
        if (userId < 0 || friendId < 0) {
            throw new ConditionsNotMetException("У пользователей id должен быть положительным");
        }
        User user = userStorage.getUserById(userId);
        User userFriend = userStorage.getUserById(friendId);

        if (user == null || userFriend == null) {
            throw new NotFoundException("Пользователь(и) не найдены в базе данных");
        }
        deleteFriend(user, userFriend);
        return userFriend;
    }


    public Collection<User> getAllFriendsByUserId(Integer userId) {
        List<Integer> userFriendsId = new ArrayList<>(getUserFriends(userStorage.getUserById(userId)));

        return userFriendsId.stream()
                .map(id -> userStorage.getUserById(id))
                .collect(Collectors.toList());

    }


    public Collection<User> getCommonFriendsOfTwoUsers(Integer userId, Integer otherUserId) {

        List<User> userFriends = (List<User>) getAllFriendsByUserId(userId);
        List<User> otherUserFriends = (List<User>) getAllFriendsByUserId(otherUserId);

        return userFriends.stream()
                .filter(user -> otherUserFriends.contains(user))
                .toList();
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
