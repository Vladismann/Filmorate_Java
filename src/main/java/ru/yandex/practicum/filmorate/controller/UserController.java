package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;

import static ru.yandex.practicum.filmorate.Messages.TechnicalMessages.*;
import static ru.yandex.practicum.filmorate.controller.Paths.USERS_PATH;

@Slf4j
@RestController
@RequestMapping(USERS_PATH)
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
    @GetMapping()
    public Collection<User> getAll() {
        log.info(RECEIVED_GET + USERS_PATH);
        log.info(RECEIVED_USERS, storage.size());
        return super.getAll();
    }

    @Override
    @PostMapping()
    public User create(@Valid @RequestBody User user) {
        log.info(RECEIVED_POST + USERS_PATH);
        return super.create(user);
    }

    @Override
    @PutMapping()
    public User update(@Valid @RequestBody User user) {
        log.info(RECEIVED_PUT + USERS_PATH);
        return super.update(user);
    }

}