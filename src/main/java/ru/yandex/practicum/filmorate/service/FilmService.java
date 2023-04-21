package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FilmService<T extends InMemoryFilmStorage, U extends InMemoryUserStorage> {

    private final T filmStorage;
    private final U userStorage;

    public FilmService(T filmStorage, U userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    private int compare(Film film1, Film film2) {
        return Integer.compare(film2.getLikes().size(), film1.getLikes().size());
    }

    public Set<Integer> addLike(int filmId, int userId) {
        userStorage.isResourceExist(userId);
        filmStorage.getById(filmId).getLikes().add(userId);
        return filmStorage.getById(filmId).getLikes();
    }

    public Set<Integer> deleteLike(int filmId, int userId) {
        userStorage.isResourceExist(userId);
        filmStorage.getById(filmId).getLikes().remove(userId);
        return filmStorage.getById(filmId).getLikes();
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.getStorage().
                values()
                .stream().
                sorted(this::compare).
                limit(count).
                collect(Collectors.toList());
    }

}