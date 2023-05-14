package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;

import static ru.yandex.practicum.filmorate.controller.Paths.USERS_PATH;
import static ru.yandex.practicum.filmorate.messages.TechnicalMessages.RECEIVED_POST;

@Slf4j
@RestController
@RequestMapping(USERS_PATH)
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /*@GetMapping()
    public Collection<User> getAll() {
        log.info(RECEIVED_GET + USERS_PATH);
        return userService.getAllUsers();
    }

    @GetMapping(GET_BY_ID_PATH)
    public User getById(@PathVariable(value = "id") int id) {
        log.info(RECEIVED_GET + USERS_PATH + id);
        return userService.getUserById(id);
    }*/

    @PostMapping()
    public User create(@Valid @RequestBody User user) {
        log.info(RECEIVED_POST + USERS_PATH);
        return userService.createUser(user);
    }

    /*@PutMapping()
    public User update(@Valid @RequestBody User user) {
        log.info(RECEIVED_PUT + USERS_PATH);
        return userService.updateUser(user);
    }

    @PutMapping(UPDATE_FRIEND_PATH)
    public void addFriend(
            @PathVariable(value = "id") int id,
            @PathVariable(value = "friendId") int friendId) {
        log.info(RECEIVED_PUT + UPDATE_FRIEND_PATH);
        userService.addFriend(id, friendId);
    }

    @DeleteMapping(UPDATE_FRIEND_PATH)
    public void deleteFriend(
            @PathVariable(value = "id") int id,
            @PathVariable(value = "friendId") int friendId) {
        log.info(RECEIVED_PUT + UPDATE_FRIEND_PATH);
        userService.deleteFriend(id, friendId);
    }

    @GetMapping(GET_USER_FRIENDS_PATH)
    public Collection<User> getUserFriends(@PathVariable(value = "id") int id) {
        log.info(RECEIVED_GET + GET_USER_FRIENDS_PATH);
        return userService.getUserFriends(id);
    }

    @GetMapping(GET_COMMON_FRIENDS_PATH)
    public Collection<User> getCommonFriends(
            @PathVariable(value = "id") int id,
            @PathVariable(value = "otherId") int otherId) {
        log.info(RECEIVED_GET + GET_COMMON_FRIENDS_PATH);
        return userService.findCommonFriends(id, otherId);
    }*/

}