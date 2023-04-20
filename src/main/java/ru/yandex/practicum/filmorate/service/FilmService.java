package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

@Service
public class FilmService <T extends InMemoryFilmStorage> {

    private final T filmStorage;

    @Autowired
    public FilmService(T filmStorage) {
        this.filmStorage = filmStorage;
    }


}
