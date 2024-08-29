package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.JdbcUserRepository;

import java.util.Collection;
import java.util.Optional;

@Component
//@Qualifier("UserDbStorage")
public class UserDbStorage implements UserStorage {

    @Autowired
    private final JdbcUserRepository jdbcUserRepository;

    @Autowired
    public UserDbStorage(JdbcUserRepository jdbcUserRepository) {
        this.jdbcUserRepository = jdbcUserRepository;
    }

    @Override
    public User create(User user) {

        Optional<User> alreadyexistUser = jdbcUserRepository.getUserByEmail(user.getEmail());

        if (alreadyexistUser.isPresent()) {
            throw new DuplicatedDataException("Данный пользователь уже создан!");
        }
        return jdbcUserRepository.create(user);
    }

    @Override
    public User update(User newUser) {

        Optional<User> existingUserById = jdbcUserRepository.getUserById(newUser.getId());
        if (existingUserById.isEmpty()) {
            throw new NotFoundException("Пользователь с таким id не найден!");
        }

        Optional<User> existingUserByEmail = jdbcUserRepository.getUserByEmail(newUser.getEmail());
        if (existingUserByEmail.isPresent() && !existingUserByEmail.get().getId().equals(newUser.getId())) {
            throw new DuplicatedDataException("Другой пользователь с таким email уже существует!");
        }

        User updatedUser = jdbcUserRepository.update(newUser);

        System.out.println("Пользователь обновлен: " + updatedUser);

        return updatedUser;
    }


    @Override
    public Collection<User> getAll() {
        return jdbcUserRepository.getAll();
    }

    @Override
    public User delete(User user) {
        return jdbcUserRepository.delete(user);
    }

    @Override
    public Optional<User> getUserById(Integer id) {
        if (id < 1) {
            throw new ConditionsNotMetException("Идентификатор пользователя не может быть равен нулю!");
        }
        return jdbcUserRepository.getUserById(id);

    }

}





























