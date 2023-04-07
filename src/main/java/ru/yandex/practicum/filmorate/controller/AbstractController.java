package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Resource;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static ru.yandex.practicum.filmorate.Messages.TechnicalMessages.*;

@Slf4j
public abstract class AbstractController<T extends Resource> {

    protected final Map<Integer, T> storage = new HashMap<>();
    private int id = 1;

    protected abstract void validateResource(T resource);


    public Collection<T> getAll() {
        return storage.values();
    }

    public T create(T resource) {
        validateResource(resource);
        resource.setId(id);
        storage.put(id++, resource);
        log.info(ADDED_RESOURCE, resource);
        return resource;
    }

    public T update(T resource) {
        validateResource(resource);
        int resourceId = resource.getId();
        if (!storage.containsKey(resourceId) || resourceId == 0) {
            log.info(RESOURCE_NOT_FOUND, resource);
            throw new ResponseStatusException(NOT_FOUND, RESOURCE_NOT_FOUND_EX);
        }
        storage.put(resourceId, resource);
        log.info(UPDATED_RESOURCE, resource);
        return resource;
    }

}
