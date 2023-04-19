package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import javax.validation.Valid;
import java.util.Collection;

import static ru.yandex.practicum.filmorate.controller.Paths.USERS_PATH;

@Slf4j
@RestController
@RequestMapping(USERS_PATH)
public class UserController {

    private final InMemoryUserStorage userStorage;

    @Autowired
    public UserController(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @GetMapping()
    public Collection<User> getAll() {
        return userStorage.getAll();
    }

    @PostMapping()
    public User create(@Valid @RequestBody User user) {
        return userStorage.create(user);
    }

    @PutMapping()
    public User update(@Valid @RequestBody User user) {
        return userStorage.update(user);
    }

}