package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

import static ru.yandex.practicum.filmorate.controller.Paths.*;

@Slf4j
@RestController
@RequestMapping(FILMS_PATH)
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping()
    public Collection<Film> getAll() {
        return filmService.getAll();
    }

    @GetMapping(GET_BY_ID_PATH)
    public Film getById(@PathVariable(value = "id") int id) {
        return filmService.getFilmById(id);
    }

    @PostMapping()
    public Film create(@Valid @RequestBody Film film) {
        return filmService.createFilm(film);
    }

    @PutMapping()
    public Film update(@Valid @RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    @PutMapping(UPDATE_LIKE_PATH)
    public void addLike(
            @PathVariable(value = "id") int id,
            @PathVariable(value = "userId") int userId) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping(UPDATE_LIKE_PATH)
    public void deleteLike(
            @PathVariable(value = "id") int id,
            @PathVariable(value = "userId") int userId) {
        filmService.deleteLike(id, userId);
    }

    @GetMapping(GET_POPULAR_FILMS_PATH)
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        return filmService.getPopularFilms(count);
    }
}