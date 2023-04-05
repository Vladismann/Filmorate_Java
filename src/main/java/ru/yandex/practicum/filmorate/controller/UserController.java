package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;


import java.util.Collection;
import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final HashMap<Integer, User> users = new HashMap<>();
    private int id = 1;

    @GetMapping()
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @PostMapping()
    public User createUser(@RequestBody User user) throws ValidationException {
        /*if (users.containsKey(user.getEmail())) {
            throw new ValidationException(user.getEmail() + " уже недоступен");
        } else if (user.getEmail() == null || user.getEmail().isEmpty() || user.getEmail().isBlank()) {
            throw new ValidationException("Укажите корректный email");
        }*/
        user.setId(id);
        users.put(id++, user);
        return user;
    }

    /*@PutMapping()
    public User updateUser(@RequestBody User user) throws ValidationException {
        if (user.getEmail() == null || user.getEmail().isEmpty() || user.getEmail().isBlank()) {
            if (!users.containsKey(user.getEmail())) {
                throw new ValidationException("Укажите корректный email");
            }
        }
        users.put(user.getEmail(), user);
        return user;
    }*/
}