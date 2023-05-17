package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.Collection;

import static ru.yandex.practicum.filmorate.messages.TechnicalMessages.*;

@Service
@Slf4j
public class FilmService {

    public static final LocalDate CINEMA_BIRTHDAY = LocalDate.of(1895, 12, 28);
    private final FilmStorage filmStorage;
    //private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    private void validateFilm(Film film) {
        if (film.getReleaseDate().isBefore(CINEMA_BIRTHDAY)) {
            log.info(INCORRECT_FILM_DATE, film.getReleaseDate());
            throw new ValidationException(film.getReleaseDate() + INCORRECT_FILM_DATE_EX);
        }
    }

    /*private int compare(Film film1, Film film2) {
        return Integer.compare(film2.getLikes().size(), film1.getLikes().size());
    }*/

    public Film createFilm(Film film) {
        validateFilm(film);
        return filmStorage.createFilm(film);
    }

    public Film getFilmById(int id) {
        return filmStorage.getFilmById(id);
    }


    public Collection<Film> getAll() {
        log.info(RECEIVED_FILMS, filmStorage.getAllFilms().size());
        return filmStorage.getAllFilms();
    }


    public Film updateFilm(Film film) {
        validateFilm(film);
        return filmStorage.updateFilm(film);
    }

    public void addLike(int filmId, int userId) {
        filmStorage.addLikeToFilm(filmId, userId);
        log.info(ADDED_LIKE, filmId, userId);
    }

    public void deleteLike(int filmId, int userId) {
        filmStorage.deleteLikeToFilm(filmId, userId);
        log.info(DELETED_LIKE, filmId, userId);
    }

    /*public List<Film> getPopularFilms(int count) {
        log.info(GET_POPULAR_FILMS, count);
        return filmStorage.getAll()
                .stream()
                .sorted(this::compare)
                .limit(count)
                .collect(Collectors.toList());
    }*/

}