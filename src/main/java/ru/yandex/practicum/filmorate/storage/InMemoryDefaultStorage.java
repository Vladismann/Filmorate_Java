package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Resource;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static ru.yandex.practicum.filmorate.messages.TechnicalMessages.*;

@Slf4j
public abstract class InMemoryDefaultStorage<T extends Resource> implements DefaultStorage<T> {

    private final Map<Integer, T> storage = new HashMap<>();
    private int id = 1;


    @Override
    public void isResourceExist(int id) {
        if (!storage.containsKey(id) || id == 0) {
            log.info(RESOURCE_NOT_FOUND, id);
            throw new NotFoundException(RESOURCE_NOT_FOUND_EX + id);
        }
    }

    @Override
    public Collection<T> getAll() {
        return storage.values();
    }

    @Override
    public T getById(int id) {
        isResourceExist(id);
        log.info(GETTING_RESOURCE, storage.get(id));
        return storage.get(id);
    }

    @Override
    public T create(T resource) {
        resource.setId(id);
        storage.put(id, resource);
        id++;
        log.info(ADDED_RESOURCE, resource);
        return resource;
    }

    @Override
    public T update(T resource) {
        int resourceId = resource.getId();
        isResourceExist(resourceId);
        storage.put(resourceId, resource);
        log.info(UPDATED_RESOURCE, resource);
        return resource;
    }
}