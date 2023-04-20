package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.Set;

import static ru.yandex.practicum.filmorate.messages.TechnicalMessages.*;

@Service
public class UserService {

    private final InMemoryUserStorage userStorage;

    @Autowired
    public UserService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Set<Integer> addFriend(int currentUserId, int friendId) {
        userStorage.isResourceExist(currentUserId);
        userStorage.isResourceExist(friendId);
        User user = userStorage.getStorage().get(currentUserId);
        user.getFriends().add(friendId);
        return user.getFriends();
    }

    public Set<Integer> deleteFriend(int currentUserId, int friendId) {
        userStorage.isResourceExist(currentUserId);
        User user = userStorage.getStorage().get(currentUserId);
        if (!user.getFriends().contains(friendId)) {
            throw new NotFoundException(FRIEND_NOT_FOUND_EX + friendId);
        }
        user.getFriends().remove(friendId);
        return user.getFriends();
    }



}