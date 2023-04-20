package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.Set;

import static ru.yandex.practicum.filmorate.messages.TechnicalMessages.RESOURCE_NOT_FOUND_EX;

@Service
public class UserService {

    private final InMemoryUserStorage userStorage;

    @Autowired
    public UserService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    private void isUsersExists(int currentUserId, int friendId) {
        if (!userStorage.getStorage().containsKey(currentUserId)) {
            throw new NotFoundException(RESOURCE_NOT_FOUND_EX + currentUserId);
        }
        if (!userStorage.getStorage().containsKey(friendId)) {
            throw new NotFoundException(RESOURCE_NOT_FOUND_EX + friendId);
        }
    }

    public Set<Integer> addFriend(int currentUserId, int friendId) {
        isUsersExists(currentUserId, friendId);
        User user = userStorage.getStorage().get(currentUserId);
        user.getFriends().add(friendId);
        return user.getFriends();
    }
}
