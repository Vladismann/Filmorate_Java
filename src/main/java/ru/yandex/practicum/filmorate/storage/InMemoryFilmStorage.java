package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

import static ru.yandex.practicum.filmorate.controller.Paths.FILMS_PATH;
import static ru.yandex.practicum.filmorate.messages.TechnicalMessages.*;
import static ru.yandex.practicum.filmorate.service.FilmService.CINEMA_BIRTHDAY;

@Slf4j
@Component
public class InMemoryFilmStorage extends ResourceStorage<Film> {

    @Override
    protected void validateResource(Film film) {
        if (film.getReleaseDate().isBefore(CINEMA_BIRTHDAY)) {
            log.info(INCORRECT_FILM_DATE, film.getReleaseDate());
            throw new ValidationException(film.getReleaseDate() + INCORRECT_FILM_DATE_EX);
        }
    }

    public Collection<Film> getAll() {
        log.info(RECEIVED_GET + FILMS_PATH);
        log.info(RECEIVED_FILMS, storage.size());
        return super.getAll();
    }

    public Film create(Film film) {
        log.info(RECEIVED_POST + FILMS_PATH);
        return super.create(film);
    }

    public Film update(Film film) {
        log.info(RECEIVED_PUT + FILMS_PATH);
        return super.update(film);
    }
}