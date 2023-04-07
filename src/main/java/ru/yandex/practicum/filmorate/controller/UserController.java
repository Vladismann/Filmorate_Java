package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;


import javax.validation.Valid;
import java.util.Collection;

import static ru.yandex.practicum.filmorate.Messages.TechnicalMessages.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController extends AbstractController<User> {

    @Override
    protected void validateResource(User user) {
        if (user.getLogin().contains(" ")) {
            log.info(LOGIN_WITH_WHITESPACE, user.getLogin());
            throw new ValidationException(LOGIN_WITH_WHITESPACE_EX);
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info(USER_HAS_EMPTY_NAME, user.getName());
        }
    }

    @Override
    public Collection<User> getAll() {
        log.info(RECEIVED_USERS, storage.size());
        return super.getAll();
    }

    @Override
    public User create(@Valid @RequestBody User user) {
        return super.create(user);
    }

    @Override
    public User update(@Valid @RequestBody User user) {
        return super.update(user);
    }

}