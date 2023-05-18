package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.Collection;

import static ru.yandex.practicum.filmorate.controller.Paths.GENRES_PATH;
import static ru.yandex.practicum.filmorate.controller.Paths.GET_BY_ID_PATH;
import static ru.yandex.practicum.filmorate.messages.TechnicalMessages.RECEIVED_GET;

@Slf4j
@RestController
@RequestMapping(GENRES_PATH)
public class GenreController {

    private final GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping()
    public Collection<Genre> getAll() {
        log.info(RECEIVED_GET + GENRES_PATH);
        return genreService.getAllGenres();
    }

    @GetMapping(GET_BY_ID_PATH)
    public Genre getById(@PathVariable(value = "id") int id) {
        log.info(RECEIVED_GET + GENRES_PATH + id);
        return genreService.getGenreById(id);
    }
}
