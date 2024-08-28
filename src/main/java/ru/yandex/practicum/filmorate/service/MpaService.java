package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.MpaRepository;

import java.util.Collection;

@Service
public class MpaService {

    private final MpaRepository mpaRepository;

    @Autowired
    public MpaService(MpaRepository mpaRepository) {
        this.mpaRepository = mpaRepository;
    }

    public Collection<Mpa> getAll() {
        return mpaRepository.getAll();
    }

    public Mpa getMpaById(int mpaId) {
        if (mpaRepository.getById(mpaId).isEmpty()) {
            throw new NotFoundException("Такого рейтинга с id: " + mpaId + " не существует!");
        }
        return mpaRepository.getById(mpaId).get();
    }



}
