package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;


import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static ru.yandex.practicum.filmorate.model.TechnicalMessages.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final HashMap<Integer, User> users = new HashMap<>();
    private int id = 1;

    private void checkUserIsCorrect(User user) {
        if (user.getLogin().contains(" ")) {
            log.info(LOGIN_WITH_WHITESPACE, user.getLogin());
            throw new ValidationException(LOGIN_WITH_WHITESPACE_EX);
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info(USER_HAS_EMPTY_NAME, user.getName());
        }
    }

    @GetMapping()
    public Collection<User> getAllUsers() {
        log.info(RECEIVED_USERS, users.size());
        return users.values();
    }

    @PostMapping()
    public User createUser(@Valid @RequestBody User user) {
        checkUserIsCorrect(user);
        user.setId(id);
        users.put(id++, user);
        log.info(ADDED_USER, user);
        return user;

    }

    @PutMapping()
    public User updateUser(@Valid @RequestBody User user) {
        checkUserIsCorrect(user);
        int userId = user.getId();
        if (!users.containsKey(userId) || userId == 0) {
            log.info(USER_NOT_FOUND, userId);
            throw new ResponseStatusException(NOT_FOUND, USER_NOT_FOUND_EX);
        }
        users.put(userId, user);
        log.info(UPDATED_USER, user);
        return user;
    }

}