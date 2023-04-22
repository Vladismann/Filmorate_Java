package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService<T extends InMemoryFilmStorage, U extends InMemoryUserStorage> {

    public static final LocalDate CINEMA_BIRTHDAY = LocalDate.of(1895, 12, 28);
    private final T filmStorage;
    private final U userStorage;

    public FilmService(T filmStorage, U userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    private int compare(Film film1, Film film2) {
        return Integer.compare(film2.getLikes().size(), film1.getLikes().size());
    }

    public void addLike(int filmId, int userId) {
        userStorage.isResourceExist(userId);
        filmStorage.getById(filmId).getLikes().add(userId);
        log.info("Фильму с id: " + filmId + " добавлен лайк пользователя с id: " + userId);
    }

    public void deleteLike(int filmId, int userId) {
        userStorage.isResourceExist(userId);
        filmStorage.getById(filmId).getLikes().remove(userId);
        log.info("Фильму с id: " + filmId + " удален лайк пользователя с id: " + userId);
    }

    public List<Film> getPopularFilms(int count) {
        log.info("запрос популярных фильмов в количестве: " + count);
        return filmStorage.getStorage()
                .values()
                .stream()
                .sorted(this::compare)
                .limit(count)
                .collect(Collectors.toList());
    }

}