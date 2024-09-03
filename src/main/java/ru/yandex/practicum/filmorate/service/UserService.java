package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.FriendRepository;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;
    private final FriendRepository friendRepository;

    public User create(User user) {
        validateUserData(user);
        return userStorage.create(user);
    }

    public User update(User user) {
        validateUserData(user);
        return userStorage.update(user);
    }

    public Collection<User> getAll() {
        return userStorage.getAll();
    }

    public User delete(User user) {
        validateUserData(user);
        return userStorage.delete(user);
    }

    public void addFriend(int userId, int friendId) {
        if (userId < 1 || friendId < 1) {
            throw new ConditionsNotMetException("Идентификатор пользователя должен быть положительным числом");
        }

        Optional<User> userOptional = userStorage.getUserById(userId);
        Optional<User> friendOptional = userStorage.getUserById(friendId);

        if (userOptional.isEmpty() || friendOptional.isEmpty()) {
            throw new NotFoundException("Пользователь не найден");
        }

        friendRepository.addFriend(userId, friendId);
    }

    public void removeFriend(int userId, int friendId) {
        if (userId < 1 || friendId < 1) {
            throw new ConditionsNotMetException("Идентификатор пользователя должен быть положительным числом");
        }

        Optional<User> userOptional = userStorage.getUserById(userId);
        Optional<User> friendOptional = userStorage.getUserById(friendId);

        if (userOptional.isEmpty() || friendOptional.isEmpty()) {
            throw new NotFoundException("Пользователь не найден");
        }

        friendRepository.removeFriend(userId, friendId);
    }

    public Collection<User> getAllFriendsByUserId(int userId) {
        if (userId < 1) {
            throw new ConditionsNotMetException("Идентификатор пользователя должен быть положительным числом");
        }

        Optional<User> optionalUser = userStorage.getUserById(userId);

        if (optionalUser.isEmpty()) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }

        return friendRepository.getFriends(userId);
    }

    public Collection<User> getCommonFriendsOfTwoUsers(int userId, int otherUserId) {
        if (userId < 1 || otherUserId < 1) {
            throw new ConditionsNotMetException("Идентификатор пользователя должен быть положительным числом");
        }

        return friendRepository.getCommonFriends(userId, otherUserId);
    }

    public boolean isUserValid(User user) {
        return user.getId() > 0;
    }

    public User addUserFriendById(Integer userId, Integer friendId) {
        if (userId < 1 || friendId < 1) {
            throw new ConditionsNotMetException("Идентификатор пользователя должен быть положительным числом");
        }

        Optional<User> userOptional = userStorage.getUserById(userId);
        Optional<User> friendOptional = userStorage.getUserById(friendId);

        if (userOptional.isEmpty() || friendOptional.isEmpty()) {
            throw new NotFoundException("Пользователь не найден");
        }

        friendRepository.addFriend(userId, friendId);

        return friendOptional.get();
    }

    public User deleteUserFriendById(Integer userId, Integer friendId) {
        if (userId < 1 || friendId < 1) {
            throw new ConditionsNotMetException("Идентификатор пользователя должен быть положительным числом");
        }

        Optional<User> userOptional = userStorage.getUserById(userId);
        Optional<User> friendOptional = userStorage.getUserById(friendId);

        if (userOptional.isEmpty() || friendOptional.isEmpty()) {
            throw new NotFoundException("Пользователь не найден");
        }

        friendRepository.removeFriend(userId, friendId);

        return friendOptional.get();
    }

    private void validateUserData(User user) {

        if (user.getLogin().contains(" ")) {
            throw new ConditionsNotMetException("Логин должен быть указан!");
        }
    }

}
