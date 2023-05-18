package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

import static ru.yandex.practicum.filmorate.messages.TechnicalMessages.*;
import static ru.yandex.practicum.filmorate.query.FilmQuery.*;

@Component
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageImpl implements FilmDbStorage {

    private final JdbcTemplate jdbcTemplate;
    private final UserDbStorage userStorage;

    private LinkedHashSet<Genre> getFilmGenres(int filmId) {
        log.info(GET_FILM_GENRES, filmId);
        return new LinkedHashSet<>(jdbcTemplate.query(getFilmGenresIdsQuery(filmId), (rs, rowNum) -> {
            int id = rs.getInt("genre_id");
            String genreName = rs.getString("genre_name");
            return new Genre(id, genreName);
        }));
    }

    private Film createFilmFromRowArgs(SqlRowSet set) {
        int id = set.getInt("film_id");
        String filmName = set.getString("name");
        String filmDescription = set.getString("description");
        LocalDate releaseDate = set.getDate("release_date").toLocalDate();
        int duration = set.getInt("duration");
        int ratingId = set.getInt("rating_id");
        String ratingName = set.getString("rating_name");
        return Film.builder()
                .id(id)
                .name(filmName)
                .description(filmDescription)
                .releaseDate(releaseDate)
                .duration(duration)
                .mpa(new MPA(ratingId, ratingName))
                .build();
    }

    private Film createFilmFromRowArgs(ResultSet set) throws SQLException {
        int id = set.getInt("film_id");
        String filmName = set.getString("name");
        String filmDescription = set.getString("description");
        LocalDate releaseDate = set.getDate("release_date").toLocalDate();
        int duration = set.getInt("duration");
        int ratingId = set.getInt("rating_id");
        String ratingName = set.getString("rating_name");
        return Film.builder()
                .id(id)
                .name(filmName)
                .description(filmDescription)
                .releaseDate(releaseDate)
                .duration(duration)
                .mpa(new MPA(ratingId, ratingName))
                .build();
    }

    @Override
    public Film getFilmById(int id) {
        SqlRowSet createdRows = jdbcTemplate.queryForRowSet(GET_FILM_BY_ID, id);
        if (createdRows.next()) {
            Film film = createFilmFromRowArgs(createdRows);
            LinkedHashSet<Genre> genres = getFilmGenres(id);
            film.setGenres(genres);
            log.info(FILM_FOUND_ID, film);
            return film;
        } else {
            log.info(FILM_NOT_FOUND_ID, id);
            throw new NotFoundException(FILM_NOT_FOUND_ID_EX + id);
        }
    }

    public List<Film> getAllFilms() {
        return jdbcTemplate.query(GET_ALL_FILMS, (rs, rowNum) -> {
            Film film = createFilmFromRowArgs(rs);
            LinkedHashSet<Genre> genres = getFilmGenres(film.getId());
            film.setGenres(genres);
            return film;
        });
    }

    private void addFilmGenres(Set<Genre> genres, int filmId) {
        List<Genre> listGenres = new ArrayList<>(genres);
        int[] createdRows = jdbcTemplate.batchUpdate(ADD_FILM_GENRES, new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement statement, int i) throws SQLException {
                statement.setString(1, String.valueOf(filmId));
                statement.setString(2, String.valueOf(listGenres.get(i).getId()));
                log.info(FILM_ADDED_GENRE, filmId, listGenres.get(i).getId());
            }

            public int getBatchSize() {
                return listGenres.size();
            }
        });
        if (createdRows.length == 0) {
            throw new RuntimeException(FILM_GENRE_ADD_ERROR_EX + filmId + System.lineSeparator() + genres);
        }
    }

    @Override
    public Film createFilm(Film film) {
        //int createdRows = jdbcTemplate.update(CREATE_FILM, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getMpa().getId());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int createdRows = jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(CREATE_FILM, new String[]{"FILM_ID"});
            statement.setString(1, film.getName());
            statement.setString(2, film.getDescription());
            statement.setString(3, film.getReleaseDate().toString());
            statement.setString(4, String.valueOf(film.getDuration()));
            statement.setString(5, String.valueOf(film.getMpa().getId()));
            return statement;
        }, keyHolder);
        if (createdRows == 1) {
            int filmId = keyHolder.getKey().intValue();
            if (film.getGenres() != null) {
                addFilmGenres(film.getGenres(), filmId);
            }
            film.setId(filmId);
            log.info(FILM_CREATED, film);
            return film;
        } else {
            log.info(FILM_CREATION_ERROR + film);
            throw new RuntimeException(FILM_CREATION_ERROR + film);
        }
    }

    public void updateFilmGenres(int filmId, Set<Genre> newGenres) {
        if (!getFilmGenres(filmId).isEmpty()) {
            int createdRowsOne = jdbcTemplate.update(DELETE_OLD_GENRES, filmId);
            if (createdRowsOne >= 1) {
                log.info(DELETE_FILM_GENRES, filmId);
            } else {
                throw new RuntimeException(DELETE_FILM_GENRES_EX + filmId);
            }
        }
        if (newGenres != null && newGenres.size() > 0) {
            addFilmGenres(newGenres, filmId);
        }
    }

    @Override
    public Film updateFilm(Film film) {
        int filmId = film.getId();
        getFilmById(filmId);
        int createdRows = jdbcTemplate.update(
                UPDATE_FILM, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getMpa().getId(), filmId);
        updateFilmGenres(filmId, film.getGenres());
        if (createdRows == 1) {
            return getFilmById(filmId);
        } else {
            log.info(FILM_UPDATE_ERROR + film);
            throw new RuntimeException(FILM_UPDATE_ERROR + film);
        }
    }

    private boolean checkFilmLike(int filmId, int userId) {
        getFilmById(filmId);
        userStorage.getUserById(userId);
        boolean isExist = false;
        SqlRowSet firstUserFriendship = jdbcTemplate.queryForRowSet(CHECK_LIKE, filmId, userId);
        if (firstUserFriendship.next()) {
            isExist = true;
        }
        return isExist;
    }

    @Override
    public void addLikeToFilm(int filmId, int userId) {
        if (!checkFilmLike(filmId, userId)) {
            int createdRows = jdbcTemplate.update(ADD_LIKE, filmId, userId);
            if (createdRows == 1) {
                log.info(ADDED_LIKE, filmId, userId);
            }
        } else {
            log.info(ADD_LIKE_ERROR, filmId, userId);
            throw new ValidationException(ADD_LIKE_EX + filmId + ", " + userId);
        }
    }

    @Override
    public void deleteLikeToFilm(int filmId, int userId) {
        if (checkFilmLike(filmId, userId)) {
            int createdRows = jdbcTemplate.update(DELETE_LIKE, filmId, userId);
            if (createdRows == 1) {
                log.info(DELETED_LIKE, filmId, userId);
            }
        } else {
            log.info(DELETE_LIKE_ERROR, filmId, userId);
            throw new NotFoundException(DELETE_LIKE_EX + filmId + ", " + userId);
        }
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        return jdbcTemplate.query(getPopularFilmsQuery(count), (rs, rowNum) -> {
            Film film = createFilmFromRowArgs(rs);
            LinkedHashSet<Genre> genres = getFilmGenres(film.getId());
            film.setGenres(genres);
            return film;
        });
    }

}