package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Resource;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static ru.yandex.practicum.filmorate.Messages.TechnicalMessages.*;

@Slf4j
public abstract class AbstractController<T extends Resource> {

    protected Map<Integer, T> storage = new HashMap<>();
    private int id = 1;

    protected abstract void validateResource(T resource);

    @GetMapping()
    public Collection<T> getAll() {
        return storage.values();
    }

    @PostMapping()
    public T create(T resource) {
        validateResource(resource);
        resource.setId(id);
        storage.put(id++, resource);
        if (resource instanceof Film) {
            log.info(ADDED_FILM, resource);
        } else {
            log.info(ADDED_USER, resource);
        }
        return resource;
    }

    @PutMapping()
    public T update(T resource) {
        validateResource(resource);
        int resourceId = resource.getId();
        if (!storage.containsKey(resourceId) || resourceId == 0) {
            if (resource instanceof Film) {
                log.info(FILM_NOT_FOUND, resourceId);
                throw new ResponseStatusException(NOT_FOUND, FILM_NOT_FOUND_EX);
            } else {
                log.info(USER_NOT_FOUND, resourceId);
                throw new ResponseStatusException(NOT_FOUND, USER_NOT_FOUND_EX);
            }
        }
        storage.put(resourceId, resource);
        if (resource instanceof Film) {
            log.info(UPDATED_FILM, resource);
        } else {
            log.info(UPDATED_USER, resource);
        }
        return resource;
    }

}
