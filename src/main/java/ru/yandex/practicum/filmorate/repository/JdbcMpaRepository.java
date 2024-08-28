package ru.yandex.practicum.filmorate.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;
import java.sql.ResultSet;


import java.util.List;
import java.util.Optional;

@Repository
public class JdbcMpaRepository implements MpaRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public JdbcMpaRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<Mpa> getAll() {
        String sql = "SELECT * FROM mpa";
        return namedParameterJdbcTemplate.getJdbcTemplate().query(sql, (rs, rowNum) -> {
            return new Mpa(rs.getInt("mpa_id"), rs.getString("name"));
        });
    }

    @Override
    public boolean existsById(Integer id) {
        String sql = "SELECT COUNT(*) FROM mpa WHERE mpa_id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        Integer count = namedParameterJdbcTemplate.queryForObject(sql, params, Integer.class);

        return count != null && count > 0;
    }

    public Optional<Mpa> getById(int id) {
        String sql = "SELECT * FROM mpa WHERE mpa_id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        List<Mpa> mpaList = namedParameterJdbcTemplate.query(sql, params, (rs, rowNum) -> {
            return new Mpa(rs.getInt("mpa_id"), rs.getString("name"));
        });

        if (mpaList.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(mpaList.get(0));
        }
    }

    @Override
    public Optional<Mpa> getByName(String name) {
        String sql = "SELECT * FROM mpa WHERE name = :name";
        MapSqlParameterSource params = new MapSqlParameterSource("name", name);
        List<Mpa> mpaList = namedParameterJdbcTemplate.query(sql, params, (rs, rowNum) ->
                new Mpa(rs.getInt("mpa_id"), rs.getString("name"))
        );
        return mpaList.isEmpty() ? Optional.empty() : Optional.of(mpaList.get(0));
    }
}
