package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;

import static ru.yandex.practicum.filmorate.messages.TechnicalMessages.*;
import static ru.yandex.practicum.filmorate.query.UsersQuery.*;

@Component
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDbStorageImpl implements UserDbStorage {

    private final JdbcTemplate jdbcTemplate;

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
            log.info(USER_FOUND_ID, user);
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
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int createdRows = jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(CREATE_USER, new String[]{"USER_ID"});
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getName());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getBirthday().toString());
            return statement;
        }, keyHolder);
        if (createdRows == 1) {
            user.setId(keyHolder.getKey().intValue());
            log.info(USER_CREATED, user);
            return user;
        } else {
            log.info(USER_CREATION_ERROR, user);
            throw new RuntimeException();
        }
    }

    @Override
    public User updateUser(User user) {
        int userId = user.getId();
        getUserById(userId);
        int createdRows = jdbcTemplate.update(UPDATE_USER, user.getLogin(), user.getName(), user.getEmail(), user.getBirthday(), userId);
        if (createdRows == 1) {
            return getUserById(userId);
        } else {
            log.info(USER_UPDATE_ERROR, user);
            throw new RuntimeException(USER_UPDATE_ERROR + user);
        }
    }

    private boolean checkUserFriendShip(int userId, int friendId) {
        getUserById(userId);
        getUserById(friendId);
        boolean isExist = false;
        SqlRowSet firstUserFriendship = jdbcTemplate.queryForRowSet(CHECK_FRIENDSHIP, userId, friendId);
        if (firstUserFriendship.next()) {
            isExist = true;
        }
        return isExist;
    }

    @Override
    public void addUserFriend(int userId, int friendId) {
        if (!checkUserFriendShip(userId, friendId)) {
            int createdRows = jdbcTemplate.update(ADD_FRIEND, userId, friendId);
            if (createdRows == 1) {
                log.info(ADDED_FRIEND, userId, friendId);
            }
        } else {
            log.info(CANT_ADD_FRIEND, userId, friendId);
            throw new ValidationException(CANT_ADD_FRIEND_EX + userId + ", " + friendId);
        }
    }

    @Override
    public void deleteUserFriend(int userId, int friendId) {
        if (checkUserFriendShip(userId, friendId)) {
            int createdRows = jdbcTemplate.update(DELETE_FRIEND, userId, friendId);
            if (createdRows == 1) {
                log.info(DELETED_FRIEND, userId, friendId);
            }
        } else {
            log.info(CANT_DELETE_FRIEND, userId, friendId);
            throw new NotFoundException(CANT_DELETE_FRIEND_EX + userId + ", " + friendId);
        }
    }

    @Override
    public List<User> getUserFriends(int userId) {
        log.info(GET_USER_FRIENDS, userId);
        return jdbcTemplate.query(getUserFriendsQuery(userId), (rs, rowNum) -> {
            int id = rs.getInt("user_id");
            String userLogin = rs.getString("login");
            String userName = rs.getString("name");
            String userEmail = rs.getString("email");
            LocalDate userBirthDate = rs.getDate("birth_date").toLocalDate();
            return User.builder()
                    .id(id)
                    .login(userLogin)
                    .name(userName)
                    .email(userEmail)
                    .birthday(userBirthDate).build();
        });
    }

    @Override
    public List<User> getCommonFriends(int userId, int friendId) {
        getUserById(userId);
        getUserById(friendId);
        log.info(GET_COMMON_FRIENDS, userId, friendId);
        return jdbcTemplate.query(getCommonFriendsQuery(userId, friendId), (rs, rowNum) -> {
            int id = rs.getInt("user_id");
            String userLogin = rs.getString("login");
            String userName = rs.getString("name");
            String userEmail = rs.getString("email");
            LocalDate userBirthDate = rs.getDate("birth_date").toLocalDate();
            return User.builder()
                    .id(id)
                    .login(userLogin)
                    .name(userName)
                    .email(userEmail)
                    .birthday(userBirthDate).build();
        });
    }

}