package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.controller.Paths.FILMS_PATH;
import static ru.yandex.practicum.filmorate.messages.TechnicalMessages.*;

@Service
@Slf4j
public class FilmService {

    public static final LocalDate CINEMA_BIRTHDAY = LocalDate.of(1895, 12, 28);
    private final InMemoryFilmStorage filmStorage;
    private final InMemoryUserStorage userStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage filmStorage, InMemoryUserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    private void validateFilm(Film film) {
        if (film.getReleaseDate().isBefore(CINEMA_BIRTHDAY)) {
            log.info(INCORRECT_FILM_DATE, film.getReleaseDate());
            throw new ValidationException(film.getReleaseDate() + INCORRECT_FILM_DATE_EX);
        }
    }

    private int compare(Film film1, Film film2) {
        return Integer.compare(film2.getLikes().size(), film1.getLikes().size());
    }

    public Film createFilm(Film film) {
        log.info(RECEIVED_POST + FILMS_PATH);
        validateFilm(film);
        return filmStorage.create(film);
    }

    public Film updateFilm(Film film) {
        log.info(RECEIVED_PUT + FILMS_PATH);
        validateFilm(film);
        return filmStorage.update(film);
    }

    public Collection<Film> getAll() {
        log.info(RECEIVED_GET + FILMS_PATH);
        log.info(RECEIVED_FILMS, filmStorage.getAll().size());
        return filmStorage.getAll();
    }

    public Film getFilmById(int id) {
        log.info(RECEIVED_GET + FILMS_PATH + id);
        return filmStorage.getById(id);
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
        return filmStorage.getAll()
                .stream()
                .sorted(this::compare)
                .limit(count)
                .collect(Collectors.toList());
    }

}