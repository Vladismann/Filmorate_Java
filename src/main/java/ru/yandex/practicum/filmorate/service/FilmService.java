package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.Set;

@Service
public class FilmService<T extends InMemoryFilmStorage, U extends InMemoryUserStorage> {

    private final T filmStorage;
    private final U userStorage;

    public FilmService(T filmStorage, U userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Set<Integer> addLike(int filmId, int userId) {
        userStorage.isResourceExist(userId);
        filmStorage.getById(filmId).getLikes().add(userId);
        return filmStorage.getById(filmId).getLikes();
    }
}