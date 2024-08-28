package ru.yandex.practicum.filmorate.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Repository
@Qualifier("FilmDbStorage")
public class JdbcFilmRepository implements FilmRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final MpaRepository mpaRepository;
    private final GenreRepository genreRepository;

    @Autowired
    public JdbcFilmRepository(NamedParameterJdbcTemplate jdbcTemplate, MpaRepository mpaRepository, GenreRepository genreRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaRepository = mpaRepository;
        this.genreRepository = genreRepository;
    }


    public Film create(Film film) {
        int mpaId = film.getMpa().getId();

        if (mpaId == 0) {
            Optional<Mpa> defaultMpa = mpaRepository.getById(1);
            if (defaultMpa.isPresent()) {
                Mpa mpa = defaultMpa.get();
                mpaId = mpa.getId();
                film.setMpa(mpa);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "MPA рейтинг 'G' не найден");
            }
        } else {
            Optional<Mpa> mpaOptional = mpaRepository.getById(mpaId);
            if (!mpaOptional.isPresent()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "MPA не найден");
            }

            Mpa mpa = mpaOptional.get();
            film.setMpa(mpa);
        }

        // Дальнейшая логика вставки фильма в базу данных
        String sql = "INSERT INTO films (name, description, release_date, duration, mpa_id) VALUES (:name, :description, :releaseDate, :duration, :mpaId)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", film.getName());
        params.addValue("description", film.getDescription());
        params.addValue("releaseDate", film.getReleaseDate());
        params.addValue("duration", film.getDuration());
        params.addValue("mpaId", mpaId);  // Используем корректное значение mpa_id

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sql, params, keyHolder, new String[]{"film_id"});
        film.setId(keyHolder.getKey().intValue());

        // Сохранение жанров фильма
        saveFilmGenres(film);

        return film;
    }









    @Override
    public Film save(Film film) {
        return null;
    }

    public Optional<Film> getFilmById(int id) {
        String query = "SELECT * FROM films WHERE film_id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        Film film = jdbcTemplate.query(query, params, rs -> {
            if (rs.next()) {
                return mapRowToFilm(rs);
            } else {
                return null;
            }
        });

        if (film == null) {
            return Optional.empty();
        }

        film.setGenres(new LinkedHashSet<>(genreRepository.getGenresByFilmId(film.getId())));
        return Optional.of(film);
    }

    public Film update(Film film) {
        String query = "UPDATE films SET name = :name, description = :description, release_date = :releaseDate, duration = :duration, mpa_id = :mpaId WHERE film_id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", film.getName());
        params.addValue("description", film.getDescription());
        params.addValue("releaseDate", film.getReleaseDate());
        params.addValue("duration", film.getDuration());
        params.addValue("mpaId", film.getMpa().getId());
        params.addValue("id", film.getId());

        int updatedRows = jdbcTemplate.update(query, params);
        if (updatedRows == 0) {
            throw new NotFoundException("Фильм с id " + film.getId() + " не найден");
        }

        saveFilmGenres(film);

        return film;
    }

    public List<Film> getAll() {
        String query = "SELECT * FROM films";
        List<Film> films = jdbcTemplate.query(query, (rs, rowNum) -> mapRowToFilm(rs));

        for (Film film : films) {
            film.setGenres(new LinkedHashSet<>(genreRepository.getGenresByFilmId(film.getId())));
        }

        return films;
    }

    @Override
    public void deleteById(int id) {
        String query = "DELETE FROM films WHERE film_id = :id";

        MapSqlParameterSource params = new MapSqlParameterSource("id", id);

        int deletedRows = jdbcTemplate.update(query, params);

        if (deletedRows == 0) {
            throw new NotFoundException("Фильм с id " + id + " не найден");
        }
    }


    @Override
    public void addLike(int filmId, int userId) {
        String query = "INSERT INTO film_likes (film_id, user_id) VALUES (:filmId, :userId)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("filmId", filmId);
        params.addValue("userId", userId);

        jdbcTemplate.update(query, params);
    }


    @Override
    public void removeLike(int filmId, int userId) {
        String query = "DELETE FROM film_likes WHERE film_id = :filmId AND user_id = :userId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("filmId", filmId);
        params.addValue("userId", userId);

        jdbcTemplate.update(query, params);
    }


    @Override
    public List<Film> getMostPopularFilms(int count) {
        String getMostPopularFilmsQuery = "SELECT * FROM films " +
                "COUNT(fl.user_id) AS likes_count " +
                "FROM films AS f " +
                "LEFT JOIN film_likes AS fl ON f.film_id = fl.film_id " +
                "GROUP BY f.film_id " +
                "ORDER BY likes_count DESC " +
                "LIMIT :count";
        MapSqlParameterSource params = new MapSqlParameterSource("count", count);
        List<Film> films = jdbcTemplate.query(getMostPopularFilmsQuery, params, (rs, rowNum) -> mapRowToFilm(rs));

        for (Film film : films) {
            film.setGenres(new LinkedHashSet<>(genreRepository.getGenresByFilmId(film.getId())));
        }

        return films;

    }

    private void saveFilmGenres(Film film) {
        String deleteSql = "DELETE FROM film_genres WHERE film_id = :filmId";
        jdbcTemplate.update(deleteSql, new MapSqlParameterSource("filmId", film.getId()));

        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            String insertSql = "INSERT INTO film_genres (film_id, genre_id) VALUES (:filmId, :genreId)";
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(insertSql, new MapSqlParameterSource()
                        .addValue("filmId", film.getId())
                        .addValue("genreId", genre.getId()));
            }
        }
    }

    public Optional<Film> getFilmByNameAndReleaseDate(String name, LocalDate releaseDate) {
        String sql = "SELECT * FROM films WHERE name = :name AND release_date = :releaseDate";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", name);
        params.addValue("releaseDate", releaseDate);

        try {
            Film film = jdbcTemplate.query(sql, params, rs -> {
                if (rs.next()) {
                    return mapRowToFilm(rs);
                } else {
                    return null;
                }
            });
            return Optional.ofNullable(film);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }


    private Film mapRowToFilm(ResultSet rs) throws SQLException {
        Film film = new Film();
        film.setId(rs.getInt("film_id"));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        film.setReleaseDate(rs.getDate("release_date").toLocalDate());
        film.setDuration(rs.getInt("duration"));
        film.setMpa(mpaRepository.getById(rs.getInt("mpa_id")).orElseThrow(() -> new NotFoundException("MPA не найден")));
        return film;
    }
}
