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
    public void saveFilmGenres(int filmId, List<Genre> genres) {
        // Сначала удалим старые жанры для этого фильма
        String deleteSql = "DELETE FROM film_genres WHERE film_id = :filmId";
        jdbcTemplate.update(deleteSql, new MapSqlParameterSource("filmId", filmId));

        // Затем добавим новые жанры
        if (genres != null && !genres.isEmpty()) {
            String insertSql = "INSERT INTO film_genres (film_id, genre_id) VALUES (:filmId, :genreId)";
            for (Genre genre : genres) {
                MapSqlParameterSource params = new MapSqlParameterSource();
                params.addValue("filmId", filmId);
                params.addValue("genreId", genre.getId());
                jdbcTemplate.update(insertSql, params);
            }
        }
    }

    @Override
    public boolean existsById(long id) {
        String sql = "SELECT COUNT(*) FROM genres WHERE genre_id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        Integer count = jdbcTemplate.queryForObject(sql, params, Integer.class);
        return count != null && count > 0;
    }

}
