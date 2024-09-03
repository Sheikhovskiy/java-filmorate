package ru.yandex.practicum.filmorate.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.function.UnaryOperator.identity;

@Slf4j
@Repository
public class JdbcGenreRepository implements GenreRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final JdbcTemplate jdbcTemplateNonNamed;


    public JdbcGenreRepository(NamedParameterJdbcTemplate jdbcTemplate, JdbcTemplate jdbcTemplateNonNamed) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcTemplateNonNamed = jdbcTemplateNonNamed;
    }

    @Override
    public List<Genre> getAll() {
        String sql = "SELECT * FROM genres";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            return new Genre(rs.getLong("genre_id"), rs.getString("name"));
        });
    }

    @Override
    public Optional<Genre> getById(int id) {
        String sql = "SELECT * FROM genres WHERE genre_id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        List<Genre> genres = jdbcTemplate.query(sql, params, (rs, rowNum) -> {
            return new Genre(rs.getLong("genre_id"), rs.getString("name"));
        });

        if (genres.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(genres.getFirst());
        }
    }


    @Override
    public void load(List<Film> films) {
        log.info(String.valueOf(films.size()));

        try {
        if (films.isEmpty()) {
            log.info("films empty");
            return;
        }

        final Map<Integer, Film> filmById = films.stream()
                .collect(Collectors.toMap(Film::getId, identity()));
        log.info("filmById: " + filmById);


        List<Integer> filmsIds = films.stream().map(Film::getId).collect(Collectors.toList());
        log.info("filmIds: " + filmsIds);

        if (filmsIds.isEmpty()) {
            log.info("filmIds empty");
            return;
        }


        String inSql = String.join(",", Collections.nCopies(films.size(), "?"));
        log.info(inSql);

        final String sqlQuery = "SELECT g.genre_id, g.name, fg.film_id " +
                "FROM genres AS g " +
                "INNER JOIN film_genres AS fg ON g.genre_id = fg.genre_id " +
                "WHERE fg.film_id IN (" + inSql + ")";

        jdbcTemplateNonNamed.query(sqlQuery, (rs, rowNum) -> {
            final Film film = filmById.get(rs.getInt("film_id"));
            film.getGenres().add(makeGenre(rs, 0));
            return film;
        }, films.stream().map(Film::getId).toArray());

        } catch (Exception e) {
            e.printStackTrace();
        }


    }




    private Genre makeGenre(ResultSet rs, int rowNum) throws SQLException {
        return new Genre(rs.getLong("genre_id"), rs.getString("name"));
    }

}
