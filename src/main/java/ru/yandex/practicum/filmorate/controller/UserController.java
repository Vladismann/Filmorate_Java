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

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final HashMap<Integer, User> users = new HashMap<>();
    private int id = 1;

    private boolean checkUserIsCorrect(User user) throws ValidationException {
        if (user.getLogin().contains(" ")) {
            throw new ValidationException(user.getLogin() + " логин не должен содержать пробел");
        }
        if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return true;
    }

    @GetMapping()
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @PostMapping()
    public User createUser(@Valid @RequestBody User user) throws ValidationException {
        if (checkUserIsCorrect(user)) {
            user.setId(id);
            users.put(id++, user);
            return user;
        }
        return null;
    }

    @PutMapping()
    public User updateUser(@Valid @RequestBody User user) throws ValidationException {
        int userId = user.getId();
        if (userId != 0 && checkUserIsCorrect(user)) {
            if (!users.containsKey(userId)) {
                throw new ResponseStatusException(NOT_FOUND, "Пользователь не найден");
            }
            users.put(userId, user);
            return user;
        }
        return null;
    }
}