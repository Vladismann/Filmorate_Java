package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.messages.TechnicalMessages.FRIEND_NOT_FOUND_EX;

@Service
@Slf4j
public class UserService<T extends InMemoryUserStorage> {

    private final T userStorage;

    public UserService(T userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(int currentUserId, int friendId) {
        User user = userStorage.getById(currentUserId);
        User friend = userStorage.getById(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(currentUserId);
        log.info("Пользователю с id: " + currentUserId + " добавлен друг с id: " + friendId);
    }

    public void deleteFriend(int currentUserId, int friendId) {
        User user = userStorage.getById(currentUserId);
        User friend = userStorage.getById(friendId);
        if (!user.getFriends().contains(friendId)) {
            throw new NotFoundException(FRIEND_NOT_FOUND_EX + friendId);
        }
        user.getFriends().remove(friendId);
        if (friend.getFriends().contains(friendId)) {
            friend.getFriends().remove(currentUserId);
        }
        log.info("Пользователь с id: " + currentUserId + " удалил друга с id: " + friendId);
    }

    public List<User> getUserFriends(int currentUserId) {
        User user = userStorage.getById(currentUserId);
        List<User> friends = new ArrayList<>();
        user.getFriends().forEach(friendId -> friends.add(userStorage.getById(friendId)));
        log.info("Запрос списка друзей пользователя с id: " + currentUserId);
        return friends;
    }

    public List<User> findCommonFriends(int currentUserId, int friendId) {
        Set<Integer> friendsList1 = userStorage.getById(currentUserId).getFriends();
        Set<Integer> friendsList2 = userStorage.getById(friendId).getFriends();
        Set<Integer> commonFriends = friendsList1.stream().filter(friendsList2::contains).collect(Collectors.toSet());
        List<User> friends = new ArrayList<>();
        commonFriends.forEach(id -> friends.add(userStorage.getById(id)));
        log.info("Запрос общих друзей пользователя с id: " + currentUserId + " и id: " + friendId);
        return friends;
    }

}