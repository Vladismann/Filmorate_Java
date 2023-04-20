package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static ru.yandex.practicum.filmorate.messages.TechnicalMessages.FRIEND_NOT_FOUND_EX;

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
        User friend = userStorage.getStorage().get(friendId);
        friend.getFriends().add(currentUserId);
        return user.getFriends();
    }

    public Set<Integer> deleteFriend(int currentUserId, int friendId) {
        userStorage.isResourceExist(currentUserId);
        userStorage.isResourceExist(friendId);
        User user = userStorage.getStorage().get(currentUserId);
        if (!user.getFriends().contains(friendId)) {
            throw new NotFoundException(FRIEND_NOT_FOUND_EX + friendId);
        }
        user.getFriends().remove(friendId);
        User friend = userStorage.getStorage().get(friendId);
        if (friend.getFriends().contains(friendId)) {
            friend.getFriends().remove(currentUserId);
        }
        return user.getFriends();
    }

    public List<User> getUserFriends(int currentUserId) {
        userStorage.isResourceExist(currentUserId);
        List<User> friends = new ArrayList<>();
        User user = userStorage.getStorage().get(currentUserId);
        for (int friendId : user.getFriends()) {
            friends.add(userStorage.getStorage().get(friendId));
        }
        return friends;
    }

}