package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;

import static ru.yandex.practicum.filmorate.messages.TechnicalMessages.*;
import static ru.yandex.practicum.filmorate.query.FilmQuery.CREATE_FILM;
import static ru.yandex.practicum.filmorate.query.FilmQuery.GET_FILM_BY_NAME;
import static ru.yandex.practicum.filmorate.query.UsersQuery.GET_USER_BY_LOGIN;

@Component
@Slf4j
public class FilmDbStorageImpl implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public User getFilmByName(String name) {
        SqlRowSet createdRows = jdbcTemplate.queryForRowSet(GET_FILM_BY_NAME, name);
        if (createdRows.next()) {
            int filmId = createdRows.getInt("film_id");
            String userName = createdRows.getString("description");
            LocalDate userBirthDate = createdRows.getDate("release_date").toLocalDate();
            int duration = createdRows.getInt("duration");
            int rating_id = createdRows.getInt("rating_id");
            Film film = Film.builder()
                            .build();
            //log.info(USER_FOUND_LOGIN, login, user);
            //return user;
        } else {
            //log.info(USER_NOT_FOUND_LOGIN, login);
            //throw new NotFoundException(USER_NOT_FOUND_LOGIN_EX + login);
        }
    }

    //@Override
    public User createFilm(Film film) {
        String filmName = film.getName();
        int createdRows = jdbcTemplate.update(
                CREATE_FILM, filmName, film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getMpa());
        if (createdRows == 1) {
            log.info(FILM_CREATED, film);
            //return createdUser;
        } else {
            //log.info(USER_CREATION_ERROR, user);
            throw new RuntimeException();
        }
    }


}

