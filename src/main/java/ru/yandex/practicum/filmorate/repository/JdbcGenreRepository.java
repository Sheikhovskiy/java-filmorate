package ru.yandex.practicum.filmorate.repository;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

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
    public Optional<String> getName(int id) {
        String sql = "SELECT name FROM genres WHERE genre_id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        return jdbcTemplate.query(sql, params, rs -> {
            if (rs.next()) {
                return Optional.of(rs.getString("name"));
            } else {
                return Optional.empty();
            }
        });
    }

//    @Override
//    public boolean existsById(long id) {
//        String sql = "SELECT COUNT(*) FROM genres WHERE genre_id = :id";
//        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
//        Integer count = jdbcTemplate.queryForObject(sql, params, Integer.class);
//        return count != null && count > 0;
//    }

}
