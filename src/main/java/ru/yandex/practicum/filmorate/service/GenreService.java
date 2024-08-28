package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.GenreRepository;

import java.util.Collection;

@Service
public class GenreService {

    private final GenreRepository genreRepository;

    @Autowired
    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public Collection<Genre> getAll() {
        return genreRepository.getAll();
    }

    public Genre getGenreById(int genreId) {
        return genreRepository.getById(genreId)
                .orElseThrow(() -> new NotFoundException("Жанра с таким идентификатором id: " + genreId + " не существует!"));
    }



}