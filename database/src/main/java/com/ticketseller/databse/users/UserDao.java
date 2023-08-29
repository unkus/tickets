package com.ticketseller.databse.users;

import com.ticketseller.service.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

@Component
public class UserDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final RowMapper<UserEntity> userRowMapper = (resultSet, rowNum) -> new UserEntity(
            resultSet.getLong("id"),
            resultSet.getString("login"),
            resultSet.getString("password"),
            resultSet.getString("name"),
            resultSet.getString("surname"),
            resultSet.getString("patronymic")
    );

    @Autowired
    public UserDao(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("account")
                .usingGeneratedKeyColumns("id");
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public User findById(Long id) {
        try {
            return namedParameterJdbcTemplate.queryForObject(
                    "SELECT * FROM account WHERE id = :id",
                    new MapSqlParameterSource().addValue("id", id),
                    userRowMapper
            );
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    public User findByLogin(String login) {
        try {
            return namedParameterJdbcTemplate.queryForObject(
                    "SELECT * FROM account WHERE login = :login",
                    new MapSqlParameterSource().addValue("login", login),
                    userRowMapper
            );
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    public void create(User user) {
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(user);
        simpleJdbcInsert.execute(namedParameters);
    }

    public void update(User user) {
        namedParameterJdbcTemplate.update(
                "UPDATE account SET password = :password, name = :name, surname = :surname, patronymic = :patronymic" +
                        " WHERE id = :id",
                new BeanPropertySqlParameterSource(user)
        );
    }

    public void delete(Long id) {
        namedParameterJdbcTemplate.update(
                "DELETE FROM account WHERE id = :id",
                new MapSqlParameterSource("id", id)
        );
    }

}
