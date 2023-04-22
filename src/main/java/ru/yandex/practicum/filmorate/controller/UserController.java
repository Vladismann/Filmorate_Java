package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;

import static ru.yandex.practicum.filmorate.controller.Paths.*;

@Slf4j
@RestController
@RequestMapping(USERS_PATH)
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public Collection<User> getAll() {
        return userService.getAllUsers();
    }

    @GetMapping(GET_BY_ID)
    public User getById(@PathVariable(value = "id") int id) {
        return userService.getUserById(id);
    }

    @PostMapping()
    public User create(@Valid @RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping()
    public User update(@Valid @RequestBody User user) {
        return userService.updateUser(user);
    }

    @PutMapping(UPDATE_FRIEND_PATH)
    public void addFriend(
            @PathVariable(value = "id") int id,
            @PathVariable(value = "friendId") int friendId) {
        userService.addFriend(id, friendId);
    }

    @DeleteMapping(UPDATE_FRIEND_PATH)
    public void deleteFriend(
            @PathVariable(value = "id") int id,
            @PathVariable(value = "friendId") int friendId) {
        userService.deleteFriend(id, friendId);
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