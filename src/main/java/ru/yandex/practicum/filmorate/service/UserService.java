package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

import static ru.yandex.practicum.filmorate.messages.TechnicalMessages.*;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserService {

    private final UserDbStorage userStorage;

    private void validateUser(User user) {
        if (user.getLogin().contains(" ")) {
            log.info(LOGIN_WITH_WHITESPACE, user.getLogin());
            throw new ValidationException(LOGIN_WITH_WHITESPACE_EX + user.getLogin());
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info(USER_HAS_EMPTY_NAME, user.getName());
        }
    }

    public User createUser(User user) {
        validateUser(user);
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        validateUser(user);
        return userStorage.updateUser(user);
    }

    public Collection<User> getAllUsers() {
        log.info(RECEIVED_USERS, userStorage.getAllUsers().size());
        return userStorage.getAllUsers();
    }

    public User getUserById(int id) {
        return userStorage.getUserById(id);
    }

    public void addFriend(int currentUserId, int friendId) {
        userStorage.addUserFriend(currentUserId, friendId);
        log.info(ADDED_FRIEND, friendId, currentUserId);
    }

    public void deleteFriend(int currentUserId, int friendId) {
        userStorage.deleteUserFriend(currentUserId, friendId);
    }

    public List<User> getUserFriends(int currentUserId) {
        return userStorage.getUserFriends(currentUserId);
    }

    public List<User> findCommonFriends(int currentUserId, int friendId) {
        return userStorage.getCommonFriends(currentUserId, friendId);
    }
}