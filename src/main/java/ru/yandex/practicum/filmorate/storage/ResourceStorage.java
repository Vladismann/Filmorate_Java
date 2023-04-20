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

    //Вместо интерфейсов решил использовать абстрактный класс
    //Сервисы используют типизацию от InMemoryStorages
    protected final Map<Integer, T> storage = new HashMap<>();
    private int id = 1;

    protected abstract void validateResource(T resource);

    public void isResourceExist(int id) {
        if (!storage.containsKey(id) || id == 0) {
            log.info(RESOURCE_NOT_FOUND, id);
            throw new NotFoundException(RESOURCE_NOT_FOUND_EX + id);
        }
    }

    public Collection<T> getAll() {
        return storage.values();
    }

    public T getById(int id) {
        isResourceExist(id);
        return storage.get(id);
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
        isResourceExist(resourceId);
        storage.put(resourceId, resource);
        log.info(UPDATED_RESOURCE, resource);
        return resource;
    }

    public Map<Integer, T> getStorage() {
        return storage;
    }
}