package com.dmitrii.UsersWebService.DAOs;

import com.dmitrii.UsersWebService.Models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<User> getAllUsers() {
        return jdbcTemplate.query("SELECT id, first_name, last_name, age, email FROM \"User\";",
                new BeanPropertyRowMapper(User.class));
    }

    public String getEmail(int id) {
        return jdbcTemplate.queryForObject("SELECT email " +
                "FROM \"User\" WHERE id = ?;", new Object[]{id}, String.class);
    }

    public User getUser(int id) {
        return jdbcTemplate.queryForObject("SELECT id, first_name, last_name, age, email " +
                "FROM \"User\" WHERE id = ?;", new Object[]{id}, new BeanPropertyRowMapper<>(User.class));
    }

    public User getUser(String email) {
        return jdbcTemplate.query("SELECT id, first_name, last_name, email, age FROM \"User\" WHERE email=?;",
                new Object[]{email}, new BeanPropertyRowMapper<>(User.class))
                .stream().findAny().orElseThrow(RuntimeException::new);
    }

    public User getFullUser(String email) {
        return jdbcTemplate.query("SELECT * FROM \"User\" WHERE email=?;",
                        new Object[]{email}, new BeanPropertyRowMapper<>(User.class))
                .stream().findAny().orElse(null);
    }

    public void createUser(User user) {
        // parameters => first_name, second_name, age, email, password
        jdbcTemplate.update("INSERT INTO \"User\"(first_name, last_name, age, email, password) VALUES (?,?,?,?,?);",
                user.getFirst_name(), user.getLast_name(), user.getAge(), user.getEmail(), user.getPassword());
    }

    public void updateUserData (User user) {
        jdbcTemplate.update("UPDATE \"User\" SET first_name=?, last_name=?, age=?, email=?, password=? WHERE id=?;",
                user.getFirst_name(), user.getLast_name(), user.getAge(), user.getEmail(), user.getPassword(), user.getId());
    }

    public void updateUserPassword(int id, String password) {
        jdbcTemplate.update("UPDATE \"User\" SET password=? WHERE id=?;", password, id);
    }

    public void deleteUser(int id) {jdbcTemplate.update("DELETE FROM \"User\" WHERE id=?;", id);}
    public boolean existUser(int id) {
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject("SELECT exists(Select * from \"User\" Where id=?);", new Object[]{id}, Boolean.class));
    }

    public boolean existUser(String email) {
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject("SELECT exists(SELECT * FROM \"User\" WHERE email=?);",
                new Object[]{email}, Boolean.class));
    }
}
