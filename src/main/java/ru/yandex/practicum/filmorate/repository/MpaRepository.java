package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

public interface MpaRepository {

    List<Mpa> getAll();

    Optional<Mpa> getById(int id);

    boolean existsById(Integer id);

    Optional<Mpa> getByName(String name);

}
