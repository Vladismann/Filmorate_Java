package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static ru.yandex.practicum.filmorate.controller.Paths.*;

@Slf4j
@RestController
@RequestMapping(FILMS_PATH)
public class FilmController {

    private final InMemoryFilmStorage filmStorage;
    private final FilmService<InMemoryFilmStorage, InMemoryUserStorage> filmService;

    @Autowired
    public FilmController(InMemoryFilmStorage filmStorage, InMemoryUserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.filmService = new FilmService<>(filmStorage, userStorage);
    }

    @GetMapping()
    public Collection<Film> getAll() {
        return filmStorage.getAll();
    }

    @GetMapping(GET_BY_ID)
    public Film getById(@PathVariable(value = "id") int id) {
        return filmStorage.getById(id);
    }

    @PostMapping()
    public Film create(@Valid @RequestBody Film film) {
        return filmStorage.create(film);
    }

    @PutMapping()
    public Film update(@Valid @RequestBody Film film) {
        return filmStorage.update(film);
    }

    @PutMapping(UPDATE_LIKE_PATH)
    public Set<Integer> addLike(
            @PathVariable(value = "id") int id,
            @PathVariable(value = "userId") int userId) {
        return filmService.addLike(id, userId);
    }

    @DeleteMapping(UPDATE_LIKE_PATH)
    public Set<Integer> deleteLike(
            @PathVariable(value = "id") int id,
            @PathVariable(value = "userId") int userId) {
        return filmService.deleteLike(id, userId);
    }

    @GetMapping(GET_POPULAR_FILMS)
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10", required = false) int count) {
        return filmService.getPopularFilms(count);
    }
}