package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static ru.yandex.practicum.filmorate.messages.TechnicalMessages.*;
import static ru.yandex.practicum.filmorate.query.UsersQuery.CREATE_USER;
import static ru.yandex.practicum.filmorate.query.UsersQuery.GET_USER_BY_LOGIN;

@Component
@Slf4j
public class UserDbStorageImpl implements UserDbStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public User getUserByLogin(String login) {
        SqlRowSet userCreatedRows = jdbcTemplate.queryForRowSet(GET_USER_BY_LOGIN, login);
        if (userCreatedRows.next()) {
            int userId = Integer.parseInt(userCreatedRows.getString("user_id"));
            String userName = userCreatedRows.getString("name");
            String userLogin = userCreatedRows.getString("login");
            String userEmail = userCreatedRows.getString("email");
            LocalDate userBirthDate = LocalDate.parse(userCreatedRows.getString("birth_date"));
            User user = User.builder()
                    .id(userId)
                    .name(userName)
                    .login(userLogin)
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
    public User createUser(User user) {
        String userLogin = user.getLogin();
        int createdRows = jdbcTemplate.update(
                CREATE_USER, user.getLogin(), user.getName(), user.getEmail(), user.getBirthday());
        if (createdRows == 1) {
            return getUserByLogin(userLogin);
        } else {
            log.info(USER_CREATION_ERROR + userLogin);
            throw new RuntimeException();
        }
    }

}
