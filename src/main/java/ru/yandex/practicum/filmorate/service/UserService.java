package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;

import static ru.yandex.practicum.filmorate.messages.TechnicalMessages.*;

@Service
@Slf4j
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

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

    /*public void deleteFriend(int currentUserId, int friendId) {
        User user = userStorage.getById(currentUserId);
        User friend = userStorage.getById(friendId);
        if (!user.getFriends().contains(friendId)) {
            throw new NotFoundException(FRIEND_NOT_FOUND_EX + friendId);
        }
        user.getFriends().remove(friendId);
        if (friend.getFriends().contains(friendId)) {
            friend.getFriends().remove(currentUserId);
        }
        log.info(DELETED_FRIEND, currentUserId, friendId);
    }

    public List<User> getUserFriends(int currentUserId) {
        User user = userStorage.getById(currentUserId);
        log.info(GET_USER_FRIENDS, currentUserId);
        return user.getFriends().stream().map(userStorage::getById).collect(Collectors.toList());
    }

    public List<User> findCommonFriends(int currentUserId, int friendId) {
        Set<Integer> friendsList1 = userStorage.getById(currentUserId).getFriends();
        Set<Integer> friendsList2 = userStorage.getById(friendId).getFriends();
        Set<Integer> commonFriends = friendsList1.stream().filter(friendsList2::contains).collect(Collectors.toSet());
        log.info(GET_COMMON_FRIENDS, currentUserId, friendId);
        return commonFriends.stream().map(userStorage::getById).collect(Collectors.toList());
    }*/
}