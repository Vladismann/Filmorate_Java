package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Resource;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static ru.yandex.practicum.filmorate.messages.TechnicalMessages.*;

@Slf4j
public abstract class ResourceStorage<T extends Resource> {

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
            throw new NotFoundException(RESOURCE_NOT_FOUND_EX + resourceId);
        }
        storage.put(resourceId, resource);
        log.info(UPDATED_RESOURCE, resource);
        return resource;
    }
}
