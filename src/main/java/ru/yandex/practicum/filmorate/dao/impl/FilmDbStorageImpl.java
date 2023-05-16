package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.List;

import static ru.yandex.practicum.filmorate.messages.TechnicalMessages.*;
import static ru.yandex.practicum.filmorate.query.FilmQuery.*;

@Component
@Slf4j
public class FilmDbStorageImpl implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Genre> getFilmGenres(int filmId) {
        log.info(GET_FILM_GENRES, filmId);
        return jdbcTemplate.query(getFilmGenresIdsQuery(filmId), (rs, rowNum) -> {
            int id = rs.getInt("genre_id");
            return new Genre(id);
        });
    }


    public Film getFilmByName(String name) {
        SqlRowSet createdRows = jdbcTemplate.queryForRowSet(GET_FILM_BY_NAME, name);
        if (createdRows.next()) {
            int filmId = createdRows.getInt("film_id");
            String filmDescription = createdRows.getString("description");
            LocalDate releaseDate = createdRows.getDate("release_date").toLocalDate();
            int duration = createdRows.getInt("duration");
            int rating_id = createdRows.getInt("rating_id");
            List<Genre> genres = getFilmGenres(filmId);
            Film film = Film.builder()
                    .id(filmId)
                    .name(name)
                    .description(filmDescription)
                    .releaseDate(releaseDate)
                    .duration(duration)
                    .mpa(new MPA(rating_id))
                    .genres(genres)
                    .build();
            log.info(FILM_FOUND_NAME, name, film);
            return film;
        } else {
            log.info(FILM_NOT_FOUND_NAME, name);
            throw new NotFoundException(FILM_NOT_FOUND_NAME_EX + name);
        }
    }

    //@Override
    public Film createFilm(Film film) {
        String filmName = film.getName();
        int createdRows = jdbcTemplate.update(
                CREATE_FILM, filmName, film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getMpa());
        if (createdRows == 1) {
            Film createdFilm = getFilmByName(filmName);
            log.info(FILM_CREATED, film);
            return createdFilm;
        } else {
            log.info(FILM_CREATION_ERROR, film);
            throw new RuntimeException();
        }
    }


}