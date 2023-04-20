package ru.yandex.practicum.filmorate.service;

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
public class UserService<T extends InMemoryUserStorage> {

    //В будущем для расширения наследование от Storages будет удобнее, чем от интерфейсов
    private final T userStorage;

    public UserService(T userStorage) {
        this.userStorage = userStorage;
    }

    public Set<Integer> addFriend(int currentUserId, int friendId) {
        User user = userStorage.getById(currentUserId);
        User friend = userStorage.getById(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(currentUserId);
        return user.getFriends();
    }

    public Set<Integer> deleteFriend(int currentUserId, int friendId) {
        User user = userStorage.getById(currentUserId);
        User friend = userStorage.getById(friendId);
        if (!user.getFriends().contains(friendId)) {
            throw new NotFoundException(FRIEND_NOT_FOUND_EX + friendId);
        }
        user.getFriends().remove(friendId);
        if (friend.getFriends().contains(friendId)) {
            friend.getFriends().remove(currentUserId);
        }
        return user.getFriends();
    }

    public List<User> getUserFriends(int currentUserId) {
        User user = userStorage.getById(currentUserId);
        List<User> friends = new ArrayList<>();
        for (int friendId : user.getFriends()) {
            friends.add(userStorage.getById(friendId));
        }
        return friends;
    }

    public List<User> findCommonFriends(int currentUserId, int friendId) {
        Set<Integer> friendsList1 = userStorage.getById(currentUserId).getFriends();
        Set<Integer> friendsList2 = userStorage.getById(friendId).getFriends();
        Set<Integer> commonFriends = friendsList1.stream().filter(friendsList2::contains).collect(Collectors.toSet());
        List<User> friends = new ArrayList<>();
        for (int id : commonFriends) {
            friends.add(userStorage.getById(id));
        }
        return friends;
    }

}