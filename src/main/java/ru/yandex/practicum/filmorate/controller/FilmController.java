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
import static ru.yandex.practicum.filmorate.model.Film.CINEMA_BIRTHDAY;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final HashMap<Integer, Film> films = new HashMap<>();
    private int id = 1;

    private boolean checkFilmIsCorrect(Film films) throws ValidationException {
        if (films.getReleaseDate().isBefore(CINEMA_BIRTHDAY)) {
            throw new ValidationException(films.getReleaseDate() + " указанная дата раньше первого фильма в истории кино");
        }
        return true;
    }

    @GetMapping()
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    @PostMapping()
    public Film addFilm(@Valid @RequestBody Film film) throws ValidationException {
        if (checkFilmIsCorrect(film)) {
            film.setId(id);
            films.put(id++, film);
            return film;
        }
        return null;
    }

    @PutMapping()
    public Film updateFilm(@Valid @RequestBody Film film) throws ValidationException {
        int filmId = film.getId();
        if (filmId != 0 && checkFilmIsCorrect(film)) {
            if (!films.containsKey(filmId)) {
                throw new ResponseStatusException(NOT_FOUND, "Фильм не найден");
            }
            films.put(filmId, film);
            return film;
        }
        return null;
    }
}
