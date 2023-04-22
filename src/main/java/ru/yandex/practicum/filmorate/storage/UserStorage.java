package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static ru.yandex.practicum.filmorate.controller.Paths.USERS_PATH;
import static ru.yandex.practicum.filmorate.messages.TechnicalMessages.*;

public interface UserStorage {

    Map<Integer, User> storage = new HashMap<>();
    org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(FilmStorage.class);

    default Collection<User> getAll() {
        log.info(RECEIVED_GET + USERS_PATH);
        log.info(RECEIVED_USERS, storage.size());
        return storage.values();
    }

    default void isUserExist(int id) {
        if (!storage.containsKey(id) || id == 0) {
            log.info(RESOURCE_NOT_FOUND, id);
            throw new NotFoundException(RESOURCE_NOT_FOUND_EX + id);
        }
    }

    default User getById(int id) {
        log.info(RECEIVED_GET + id);
        isUserExist(id);
        return storage.get(id);
    }

    default User create(int id, User user) {
        user.setId(id);
        storage.put(id, user);
        log.info(ADDED_RESOURCE, user);
        return user;
    }

    default User update(User user) {
        int resourceId = user.getId();
        isUserExist(resourceId);
        storage.put(resourceId, user);
        log.info(UPDATED_RESOURCE, user);
        return user;
    }
}
