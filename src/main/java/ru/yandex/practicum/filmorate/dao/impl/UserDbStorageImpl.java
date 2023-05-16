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

    private int lastUserId() {
        SqlRowSet createdRows = jdbcTemplate.queryForRowSet(GET_USER_LAST_ID);
        if (createdRows.next()) {
            return createdRows.getInt("id");
        } else {
            throw new RuntimeException(GET_MAX_ID_ERROR);
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
        int lastUserId = lastUserId();
        int createdRows = jdbcTemplate.update(CREATE_USER, user.getLogin(), user.getName(), user.getEmail(), user.getBirthday());
        int newUserId = lastUserId();
        if (createdRows == 1 && newUserId > lastUserId) {
            user.setId(newUserId);
            log.info(USER_CREATED, user);
            return user;
        } else {
            log.info(USER_CREATION_ERROR, user);
            throw new RuntimeException();
        }
    }

    public User updateUser(User user) {
        int userId = user.getId();
        getUserById(userId);
        int createdRows = jdbcTemplate.update(UPDATE_USER, user.getLogin(), user.getName(), user.getEmail(), user.getBirthday(), userId);
        if (createdRows == 1) {
            return getUserById(userId);
        } else {
            log.info(USER_UPDATE_ERROR, user);
            throw new RuntimeException();
        }
    }

    public boolean checkUserFriendShip(int userId, int friendId) {
        getUserById(userId);
        getUserById(friendId);
        boolean isExist = false;
        SqlRowSet firstUserFriendship = jdbcTemplate.queryForRowSet(CHECK_FRIENDSHIP, userId, friendId);
        if (firstUserFriendship.next()) {
            isExist = true;
        }
        return isExist;
    }

    public void addUserFriend(int userId, int friendId) {
        if (!checkUserFriendShip(userId, friendId)) {
            int createdRowsOne;
            createdRowsOne = jdbcTemplate.update(ADD_FRIEND, userId, friendId);
            if (createdRowsOne == 1) {
                log.info(ADDED_FRIEND, userId, friendId);
            }
        } else {
            log.info(CANT_ADD_FRIEND, userId, friendId);
            throw new RuntimeException(CANT_ADD_FRIEND_EX + userId + ", " + friendId);
        }
    }

    public void deleteUserFriend(int userId, int friendId) {
        if (checkUserFriendShip(userId, friendId)) {
            int createdRowsOne;
            createdRowsOne = jdbcTemplate.update(DELETE_FRIEND, userId, friendId);
            if (createdRowsOne == 1) {
                log.info(DELETED_FRIEND, userId, friendId);
            }
        } else {
            log.info(CANT_DELETE_FRIEND, userId, friendId);
            throw new RuntimeException(CANT_DELETE_FRIEND_EX + userId + ", " + friendId);
        }
    }

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

    public List<User> getCommonFriends(int userId, int friendId) {
        getUserById(userId);
        getUserById(friendId);
        List<User> userFriends = getUserFriends(userId);
        List<User> friendFriends = getUserFriends(friendId);
        userFriends.retainAll(friendFriends);
        log.info(GET_COMMON_FRIENDS, userId, friendId);
        return userFriends;
    }

}