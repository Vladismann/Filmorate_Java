package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Set;

import static ru.yandex.practicum.filmorate.controller.Paths.*;

@Slf4j
@RestController
@RequestMapping(USERS_PATH)
public class UserController {

    private final InMemoryUserStorage userStorage;
    private final UserService userService;

    @Autowired
    public UserController(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
        userService = new UserService(userStorage);
    }

    @GetMapping()
    public Collection<User> getAll() {
        return userStorage.getAll();
    }

    @GetMapping(GET_BY_ID)
    public User getById(@PathVariable(value = "id") int id) {
        return userStorage.getById(id);
    }

    @PostMapping()
    public User create(@Valid @RequestBody User user) {
        return userStorage.create(user);
    }

    @PutMapping()
    public User update(@Valid @RequestBody User user) {
        return userStorage.update(user);
    }

    @PutMapping(UPDATE_FRIEND_PATH)
    public Set<Integer> addFriend(
            @PathVariable(value = "id") int id,
            @PathVariable(value = "friendId") int friendId) {
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping(UPDATE_FRIEND_PATH)
    public Set<Integer> deleteFriend(
            @PathVariable(value = "id") int id,
            @PathVariable(value = "friendId") int friendId) {
        return userService.deleteFriend(id, friendId);
    }

    @GetMapping(GET_USER_FRIENDS)
    public Collection<User> getUserFriends(@PathVariable(value = "id") int id) {
        return userService.getUserFriends(id);
    }

    @GetMapping(GET_COMMON_FRIENDS)
    public Collection<User> getCommonFriends(
            @PathVariable(value = "id") int id,
            @PathVariable(value = "otherId") int otherId) {
        return userService.findCommonFriends(id, otherId);
    }

}