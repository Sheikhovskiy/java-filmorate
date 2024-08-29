//package ru.yandex.practicum.filmorate.storage;
//
//import org.springframework.stereotype.Component;
//import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
//import ru.yandex.practicum.filmorate.exception.NotFoundException;
//import ru.yandex.practicum.filmorate.model.User;
//
//import java.time.LocalDate;
//import java.util.Collection;
//import java.util.HashMap;
//import java.util.Optional;
//
//@Component
//public class InMemoryUserStorage implements UserStorage {
//
//    private final HashMap<Integer, User> users = new HashMap<>();
//    private Integer currentMaxUserId = 0;
//
//    @Override
//    public User create(User user) {
//        if (isValid(user)) {
//            user.setId(getNextId());
//            users.put(user.getId(), user);
//            return user;
//        }
//        return user;
//    }
//
//    @Override
//    public User update(User newUser) {
//        if (isValid(newUser)) {
//
//            if (newUser.getId() == null) {
//                throw new ConditionsNotMetException("Id пользователя должен быть указан !");
//            } else if (!users.containsKey(newUser.getId())) {
//                throw new NotFoundException("Пользователь с id " + newUser.getId() + " не существует !");
//            }
//
//            User oldUser = users.get(newUser.getId());
//
//            oldUser.setName(newUser.getName());
//            oldUser.setEmail(newUser.getEmail());
//            oldUser.setLogin(newUser.getLogin());
//            oldUser.setBirthday(newUser.getBirthday());
//
//            return oldUser;
//
//        }
//        return newUser;
//    }
//
//    @Override
//    public Collection<User> getAll() {
//        return users.values();
//    }
//
//    @Override
//    public User delete(User user) {
//        if (user.getId() == null || user.getId() < 1) {
//            throw new ConditionsNotMetException("У пользователя должен быть положительный id");
//        }
//        users.remove(user.getId());
//
//        return user;
//    }
//
//    @Override
//    public Optional<User> getUserById(Integer id) {
//        if (id < 1) {
//            throw new ConditionsNotMetException("У пользователя должен быть положительный id");
//        }
//        return Optional.ofNullable(users.get(id));
//    }
//
//
//
//
//    public boolean isValid(User user) {
//
//        if (user.getLogin().contains(" ")) {
//            throw new ConditionsNotMetException("Логин не должен содержать пробелы !");
//        } else if (user.getName() == null || user.getName().isBlank()) {
//            user.setName(user.getLogin());
//        } else if (user.getBirthday().isAfter(LocalDate.now())) {
//            throw new ConditionsNotMetException("Дата рождения не может быть в будущем");
//        }
//        return true;
//    }
//
//    private Integer getNextId() {
//        currentMaxUserId++;
//        return currentMaxUserId;
//    }
//}
