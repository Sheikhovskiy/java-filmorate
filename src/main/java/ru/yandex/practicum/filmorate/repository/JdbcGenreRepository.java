package ru.yandex.practicum.filmorate.repository;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.function.UnaryOperator.identity;
import static org.zalando.logbook.core.BodyReplacers.stream;

@Repository
public class JdbcGenreRepository implements GenreRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcGenreRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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
            return Optional.of(genres.get(0));
        }
    }

    @Override
    public List<Genre> getGenresByFilmId(int filmId) {
        String sql = "SELECT genres.genre_id, genres.name FROM genres " +
                "INNER JOIN film_genres ON genres.genre_id = film_genres.genre_id " +
                "WHERE film_genres.film_id = :filmId";
        MapSqlParameterSource params = new MapSqlParameterSource("filmId", filmId);
        return jdbcTemplate.query(sql, params, (rs, rowNum) -> {
            return new Genre(rs.getLong("genre_id"), rs.getString("name"));
        });
    }

    @Override
    public void load(List<Film> films) {

    }


//    @Override
//    public void load(List<Film> films) {
//        final Map<Integer, Film> filmById = films.stream()
//                .collect(Collectors.toMap(Film::getId, identity()));
//
//        String inSql = String.join(",", Collections.nCopies(films.size(), "?"));
//        final String sqlQuery = "SELECT g.genre_id, g.name, fg.film_id " +
//                "FROM genres AS g " +
//                "INNER JOIN films_genres AS fg ON g.genre_id = fg.genre_id " +
//                "WHERE fg.film_id IN (" + inSql + ")";
//
//        jdbcTemplate.query(sqlQuery, (rs, rowNum) -> {
//            Film film = filmById.get(rs.getInteger("film_id"));
//            if (film != null) {
//                film.addGenre(new Genre(rs.getLong("genre_id"), rs.getString("name")));
//            }
//            return film;
//        }, films.stream().map(Film::getId).toArray());
//    }


}
