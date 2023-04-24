package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Resource;

import java.util.Collection;

public interface DefaultStorage<T extends Resource> {

    void isResourceExist(int id);

    Collection<T> getAll();

    T getById(int id);

    T create(T resource);

    T update(T resource);
}