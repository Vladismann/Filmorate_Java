package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static ru.yandex.practicum.filmorate.controller.Paths.FILMS_PATH;
import static ru.yandex.practicum.filmorate.messages.TechnicalMessages.*;

public interface FilmStorage {

    org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(FilmStorage.class);
    Map<Integer, Film> storage = new HashMap<>();

    default void isFilmExist(int id) {
        if (!storage.containsKey(id) || id == 0) {
            log.info(RESOURCE_NOT_FOUND, id);
            throw new NotFoundException(RESOURCE_NOT_FOUND_EX + id);
        }
    }

    default Collection<Film> getAll() {
        log.info(RECEIVED_GET + FILMS_PATH);
        log.info(RECEIVED_FILMS, storage.size());
        return storage.values();
    }

    default Film getById(int id) {
        log.info(RECEIVED_GET + id);
        isFilmExist(id);
        return storage.get(id);
    }

    default Film create(int id, Film film) {
        log.info(RECEIVED_POST + FILMS_PATH);
        film.setId(id);
        storage.put(id, film);
        log.info(ADDED_RESOURCE, film);
        return film;
    }

    default Film update(Film film) {
        log.info(RECEIVED_PUT + FILMS_PATH);
        int resourceId = film.getId();
        isFilmExist(resourceId);
        storage.put(resourceId, film);
        log.info(UPDATED_RESOURCE, film);
        return film;
    }
}