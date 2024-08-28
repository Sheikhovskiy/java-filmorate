package ru.yandex.practicum.filmorate.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Repository
@Qualifier("FilmDbStorage")
public class JdbcFilmRepository implements FilmRepository {

    private static final Logger log = LoggerFactory.getLogger(JdbcFilmRepository.class);

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

        Optional<Film> existingFilm = getFilmByNameAndReleaseDate(film.getName(), film.getReleaseDate());
        if (existingFilm.isPresent()) {
            throw new DuplicatedDataException("Фильм с таким именем и датой релиза уже существует!");
        }

        int mpaId = film.getMpa().getId();

        log.info("СОЗДАНИЕ ФИЛЬМА С MPA ID: {}", mpaId);

        String sql = "INSERT INTO films (name, description, release_date, duration, mpa_id) VALUES (:name, :description, :releaseDate, :duration, :mpaId)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", film.getName());
        params.addValue("description", film.getDescription());
        params.addValue("releaseDate", film.getReleaseDate());
        params.addValue("duration", film.getDuration());
        params.addValue("mpaId", mpaId);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sql, params, keyHolder, new String[]{"film_id"});
        film.setId(keyHolder.getKey().intValue());

        saveFilmGenres(film);

        return film;
    }


    @Override
    public Film save(Film film) {
        return null;
    }

    @Override
    public Optional<Film> getFilmById(int id) {
        log.info("ID ФИЛЬМА: " + id);
        try {
            String query = "SELECT * FROM films WHERE film_id = :id";
            MapSqlParameterSource params = new MapSqlParameterSource("id", id);
            Film film = jdbcTemplate.query(query, params, rs -> {
                if (rs.next()) {
                    Film tempFilm = mapRowToFilm(rs);
                    System.out.println(getAll());
                    tempFilm.setGenres(new LinkedHashSet<>(genreRepository.getGenresByFilmId(tempFilm.getId())));
                    return tempFilm;
                } else {
                    return null;
                }
            });

            return Optional.ofNullable(film);
        } catch (Exception e) {
            log.error("Ошибка при попытке получить фильм с id " + id, e);
            throw new RuntimeException("Произошла ошибка при извлечении фильма", e);
        }
    }



    public Film update(Film film) {
        log.info("Updating film: {}", film);
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
        log.info("Фильм успешно обновлен: {}", film);

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
        String getMostPopularFilmsQuery = "SELECT f.*, COUNT(fl.user_id) AS likes_count " +
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

//        film.getGenres();

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
        String sql = "SELECT film_id, name, release_date FROM films WHERE name = :name AND release_date = :releaseDate";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", name);
        params.addValue("releaseDate", releaseDate);

        List<Film> films = jdbcTemplate.query(sql, params, (rs, rowNum) -> {
            Film film = new Film();
            film.setId(rs.getInt("film_id"));
            film.setName(rs.getString("name"));
            film.setReleaseDate(rs.getDate("release_date").toLocalDate());
            return film;
        });

        return films.isEmpty() ? Optional.empty() : Optional.of(films.get(0));
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
