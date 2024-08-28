package ru.yandex.practicum.filmorate.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

//ResultSet предоставляет данные, извлеченные из базы данных в виде таблицы, с которой можно работать построчно.
//Метод mapRowToUser преобразует одну строку данных из ResultSet в объект User.


//  user_id: Это имя столбца в базе данных.
// "id": Это имя параметра, которое используется для замены в SQL-запросе.
//  id: Это значение переменной, которое будет подставлено в запрос вместо :id.
@Repository
@Qualifier("userDbStorage")
public class JdbcUserRepository implements UserRepository {

    private final NamedParameterJdbcOperations jdbcTemplate;

    @Autowired
    JdbcUserRepository(NamedParameterJdbcOperations jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }



    public User create(User user) {
        String query = "INSERT INTO users (email, login, user_name, birthday) VALUES (:email, :login, :name, :birthday)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("email", user.getEmail());
        params.addValue("login", user.getLogin());
        params.addValue("name", user.getName());
        params.addValue("birthday", user.getBirthday());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(query, params, keyHolder, new String[]{"user_id"});
        if (keyHolder.getKey() != null) {
            user.setId(keyHolder.getKey().intValue());
        } else {
            throw new RuntimeException("Не удалось получить сгенерированный ID");
        }


        return user;
    }

    public User update(User newUser) {
        String query = "UPDATE users SET email = :email, login = :login, user_name = :name, birthday = :birthday WHERE user_id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("email", newUser.getEmail());
        params.addValue("login", newUser.getLogin());
        params.addValue("name", newUser.getName());
        params.addValue("birthday", newUser.getBirthday());
        params.addValue("id", newUser.getId());

        int updateRows = jdbcTemplate.update(query, params);
        if (updateRows == 0) {
            throw new NotFoundException("Пользователь с id " + newUser.getId() + " не найден");
        }
        return newUser;
    }

    public List<User> getAll() {
        String query = "SELECT * FROM users";
        return jdbcTemplate.query(query, (rs, rowNum) -> mapRowToUser(rs));
    }


    public User delete(User user) {
        String query = "DELETE FROM users WHERE user_id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource("id", user.getId());
        int deletedRows = jdbcTemplate.update(query, params);
        if (deletedRows == 0) {
            throw new NotFoundException("Пользователь с id " + user.getId() + " не найден");
        }
        return user;
    }

    public Optional<User> getUserById(Integer id) {
        String sql = "SELECT * FROM users WHERE user_id= :id";
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        User user = jdbcTemplate.query(sql, params, rs -> {
            if (rs.next()) {
                return mapRowToUser(rs);
            } else {
                return null;
            }
        });

        return Optional.ofNullable(user);
    }


    public Optional<User> getUserByEmail(String emailToCheck) {
        String query = "SELECT * FROM users WHERE email = :emailToCheck";
        MapSqlParameterSource params = new MapSqlParameterSource("emailToCheck", emailToCheck);

        List<User> users = jdbcTemplate.query(query, params, (rs, rowNum) -> mapRowToUser(rs));

        return users.stream().findFirst();
    }


    private User mapRowToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("user_id"));
        user.setEmail(rs.getString("email"));
        user.setLogin(rs.getString("login"));
        user.setName(rs.getString("user_name"));
        user.setBirthday(rs.getDate("birthday").toLocalDate());
        return user;
    }
}
