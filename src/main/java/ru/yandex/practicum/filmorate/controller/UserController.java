package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;


import java.util.Collection;
import java.util.HashMap;

@RestController
@RequestMapping("/users")
public class UserController {

    private final HashMap<Integer, User> users = new HashMap<>();

    @RequestMapping

    @GetMapping()
    public Collection<User> getAll() {
        return users.values();
    }

    @PostMapping()
    public User create(@RequestBody User user) throws ValidationException {
        if (users.containsKey(user.getId())) {
            throw new ValidationException(user.getEmail() + " уже недоступен");
        } else if (user.getEmail() == null || user.getEmail().isEmpty() || user.getEmail().isBlank()) {
            throw new ValidationException("Укажите корректный email");
        }
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping()
    public User update(@RequestBody User user) throws ValidationException {
        if (user.getEmail() == null || user.getEmail().isEmpty() || user.getEmail().isBlank()) {
            if (!users.containsKey(user.getId())) {
                throw new ValidationException("Укажите корректный email");
            }
        }
        users.put(user.getId(), user);
        return user;
    }
}
