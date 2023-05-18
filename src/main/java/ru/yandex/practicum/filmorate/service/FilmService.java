package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import static ru.yandex.practicum.filmorate.messages.TechnicalMessages.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {

    public static final LocalDate CINEMA_BIRTHDAY = LocalDate.of(1895, 12, 28);
    private final FilmDbStorage filmStorage;

    private void validateFilm(Film film) {
        if (film.getReleaseDate().isBefore(CINEMA_BIRTHDAY)) {
            log.info(INCORRECT_FILM_DATE, film.getReleaseDate());
            throw new ValidationException(film.getReleaseDate() + INCORRECT_FILM_DATE_EX);
        }
    }

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

    public List<Film> getPopularFilms(int count) {
        log.info(GET_POPULAR_FILMS, count);
        return filmStorage.getPopularFilms(count);
    }

}