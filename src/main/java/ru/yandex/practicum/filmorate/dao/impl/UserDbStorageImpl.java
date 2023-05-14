package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;

import static ru.yandex.practicum.filmorate.messages.TechnicalMessages.*;
import static ru.yandex.practicum.filmorate.query.UsersQuery.*;

@Component
@Slf4j
public class UserDbStorageImpl implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public User getUserByLogin(String login) {
        SqlRowSet userCreatedRows = jdbcTemplate.queryForRowSet(GET_USER_BY_LOGIN, login);
        if (userCreatedRows.next()) {
            int userId = userCreatedRows.getInt("user_id");
            String userName = userCreatedRows.getString("name");
            String userEmail = userCreatedRows.getString("email");
            LocalDate userBirthDate = userCreatedRows.getDate("birth_date").toLocalDate();
            User user = User.builder()
                    .id(userId)
                    .login(login)
                    .name(userName)
                    .email(userEmail)
                    .birthday(userBirthDate).build();
            log.info(USER_FOUND_LOGIN, login, user);
            return user;
        } else {
            log.info(USER_NOT_FOUND_LOGIN, login);
            throw new NotFoundException(USER_NOT_FOUND_LOGIN_EX + login);
        }
    }

    @Override
    public User getUserById(int id) {
        SqlRowSet userCreatedRows = jdbcTemplate.queryForRowSet(GET_USER_BY_ID, id);
        if (userCreatedRows.next()) {
            String userLogin = userCreatedRows.getString("login");
            String userName = userCreatedRows.getString("name");
            String userEmail = userCreatedRows.getString("email");
            LocalDate userBirthDate = userCreatedRows.getDate("birth_date").toLocalDate();
            User user = User.builder()
                    .id(id)
                    .login(userLogin)
                    .name(userName)
                    .email(userEmail)
                    .birthday(userBirthDate).build();
            log.info(USER_FOUND_ID, id, user);
            return user;
        } else {
            log.info(USER_NOT_FOUND_ID, id);
            throw new NotFoundException(USER_NOT_FOUND_ID_EX + id);
        }
    }

    @Override
    public List<User> getAllUsers() {
        return jdbcTemplate.query(GET_ALL_USERS, (rs, rowNum) -> {
            int userId = rs.getInt("user_id");
            String userLogin = rs.getString("login");
            String userName = rs.getString("name");
            String userEmail = rs.getString("email");
            LocalDate userBirthDate = rs.getDate("birth_date").toLocalDate();
            return User.builder()
                    .id(userId)
                    .login(userLogin)
                    .name(userName)
                    .email(userEmail)
                    .birthday(userBirthDate).build();
        });
    }

    @Override
    public User createUser(User user) {
        String userLogin = user.getLogin();
        int createdRows = jdbcTemplate.update(CREATE_USER, user.getLogin(), user.getName(), user.getEmail(), user.getBirthday());
        if (createdRows == 1) {
            return getUserByLogin(userLogin);
        } else {
            log.info(USER_CREATION_ERROR, user);
            throw new RuntimeException();
        }
    }

    public User updateUser(User user) {
        int userId = user.getId();
        User oldUser = getUserById(userId);
        int createdRows = 0;
        if (oldUser != null) {
            createdRows = jdbcTemplate.update(UPDATE_USER, user.getLogin(), user.getName(), user.getEmail(), user.getBirthday(), userId);
        }
        if (createdRows == 1) {
            return getUserById(userId);
        } else {
            log.info(USER_UPDATE_ERROR, user);
            throw new RuntimeException();
        }
    }

}

