package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static ru.yandex.practicum.filmorate.model.TechnicalMessages.*;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final HashMap<Integer, Film> films = new HashMap<>();
    public static final LocalDate CINEMA_BIRTHDAY = LocalDate.of(1895, 12, 28);
    private int id = 1;

    private void checkFilmIsCorrect(Film films) {
        if (films.getReleaseDate().isBefore(CINEMA_BIRTHDAY)) {
            log.info(INCORRECT_FILM_DATE, films.getReleaseDate());
            throw new ValidationException(films.getReleaseDate() + INCORRECT_FILM_DATE_EX);
        }
    }

    @GetMapping()
    public Collection<Film> getAllFilms() {
        log.info(RECEIVED_FILMS, films.size());
        return films.values();
    }

    @PostMapping()
    public Film addFilm(@Valid @RequestBody Film film) {
        checkFilmIsCorrect(film);
        film.setId(id);
        films.put(id++, film);
        log.info(ADDED_FILM, film);
        return film;
    }

    @PutMapping()
    public Film updateFilm(@Valid @RequestBody Film film) {
        checkFilmIsCorrect(film);
        int filmId = film.getId();
        if (!films.containsKey(filmId) || filmId == 0) {
            log.info(FILM_NOT_FOUND, filmId);
            throw new ResponseStatusException(NOT_FOUND, FILM_NOT_FOUND_EX);
        }
        films.put(filmId, film);
        log.info(UPDATED_FILM, film);
        return film;
    }

}