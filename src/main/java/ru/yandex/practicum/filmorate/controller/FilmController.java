package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import javax.validation.Valid;
import java.util.Collection;

import static ru.yandex.practicum.filmorate.controller.Paths.FILMS_PATH;
import static ru.yandex.practicum.filmorate.controller.Paths.GET_BY_ID;

@Slf4j
@RestController
@RequestMapping(FILMS_PATH)
public class FilmController {

    private final InMemoryFilmStorage filmStorage;

    @Autowired
    public FilmController(InMemoryFilmStorage filmStorage) {
        this.filmStorage = filmStorage;
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

}