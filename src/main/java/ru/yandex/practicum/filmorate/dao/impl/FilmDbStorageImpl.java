package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static ru.yandex.practicum.filmorate.messages.TechnicalMessages.*;
import static ru.yandex.practicum.filmorate.query.FilmQuery.*;

@Component
@Slf4j
public class FilmDbStorageImpl implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final UserStorage userStorage;

    @Autowired
    public FilmDbStorageImpl(JdbcTemplate jdbcTemplate, UserStorage userStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.userStorage = userStorage;
    }

    private int lastFilmId() {
        SqlRowSet createdRows = jdbcTemplate.queryForRowSet(GET_FILM_LAST_ID);
        if (createdRows.next()) {
            return createdRows.getInt("id");
        } else {
            throw new RuntimeException(GET_MAX_ID_ERROR);
        }
    }

    private List<Genre> getFilmGenres(int filmId) {
        log.info(GET_FILM_GENRES, filmId);
        return jdbcTemplate.query(getFilmGenresIdsQuery(filmId), (rs, rowNum) -> {
            int id = rs.getInt("genre_id");
            String genreName = rs.getString("genre_name");
            return new Genre(id, genreName);
        });
    }

    @Override
    public Film getFilmById(int id) {
        SqlRowSet createdRows = jdbcTemplate.queryForRowSet(GET_FILM_BY_ID, id);
        if (createdRows.next()) {
            String filmName = createdRows.getString("name");
            String filmDescription = createdRows.getString("description");
            LocalDate releaseDate = createdRows.getDate("release_date").toLocalDate();
            int duration = createdRows.getInt("duration");
            int ratingId = createdRows.getInt("rating_id");
            String ratingName = createdRows.getString("rating_name");
            List<Genre> genres = getFilmGenres(id);
            Film film = Film.builder()
                    .id(id)
                    .name(filmName)
                    .description(filmDescription)
                    .releaseDate(releaseDate)
                    .duration(duration)
                    .mpa(new MPA(ratingId, ratingName))
                    .genres(genres)
                    .build();
            log.info(FILM_FOUND_ID, film);
            return film;
        } else {
            log.info(FILM_NOT_FOUND_ID, id);
            throw new NotFoundException(FILM_NOT_FOUND_ID_EX + id);
        }
    }

    public List<Film> getAllFilms() {
        return jdbcTemplate.query(GET_ALL_FILMS, (rs, rowNum) -> {
            int id = rs.getInt("film_id");
            String filmName = rs.getString("name");
            String filmDescription = rs.getString("description");
            LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
            int duration = rs.getInt("duration");
            int ratingId = rs.getInt("rating_id");
            String ratingName = rs.getString("rating_name");
            List<Genre> genres = getFilmGenres(id);
            return Film.builder()
                    .id(id)
                    .name(filmName)
                    .description(filmDescription)
                    .releaseDate(releaseDate)
                    .duration(duration)
                    .mpa(new MPA(ratingId, ratingName))
                    .genres(genres)
                    .build();
        });
    }

    private void addFilmGenres(List<Genre> genres, int filmId) {
        Set<Genre> uniqueGenres = new LinkedHashSet<>(genres);
        for (Genre genre : uniqueGenres) {
            try {
                int genreId = genre.getId();
                int createdRows = jdbcTemplate.update(ADD_FILM_GENRES, filmId, genreId);
                if (createdRows == 0) {
                    throw new RuntimeException(FILM_GENRE_ADD_ERROR_EX + filmId + System.lineSeparator() + genres);
                } else {
                    log.info(FILM_ADDED_GENRE, filmId, genreId);
                }
            } catch (RuntimeException exception) {
                log.error(exception.getMessage());
            }
        }
    }

    //@Override
    public Film createFilm(Film film) {
        int lastFilmId = lastFilmId();
        int createdRows = jdbcTemplate.update(
                CREATE_FILM, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getMpa().getId());
        int newFilmId = lastFilmId();
        if (createdRows == 1 && newFilmId > lastFilmId) {
            if (film.getGenres() != null) {
                addFilmGenres(film.getGenres(), newFilmId);
            }
            film.setId(newFilmId);
            log.info(FILM_CREATED, film);
            return film;
        } else {
            log.info(FILM_CREATION_ERROR + film);
            throw new RuntimeException(FILM_CREATION_ERROR + film);
        }
    }

    public void updateFilmGenres(int filmId, List<Genre> newGenres) {
        if (!getFilmGenres(filmId).isEmpty()) {
            int createdRowsOne = jdbcTemplate.update(DELETE_OLD_GENRES, filmId);
            if (createdRowsOne >= 1) {
                log.info(DELETE_FILM_GENRES, filmId);
            } else {
                throw new RuntimeException(DELETE_FILM_GENRES_EX + filmId);
            }
        }
        if (newGenres != null) {
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
            int id = rs.getInt("film_id");
            String filmName = rs.getString("name");
            String filmDescription = rs.getString("description");
            LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
            int duration = rs.getInt("duration");
            int ratingId = rs.getInt("rating_id");
            String ratingName = rs.getString("rating_name");
            int likes = rs.getInt("likes");
            List<Genre> genres = getFilmGenres(id);
            return Film.builder()
                    .id(id)
                    .name(filmName)
                    .description(filmDescription)
                    .releaseDate(releaseDate)
                    .duration(duration)
                    .mpa(new MPA(ratingId, ratingName))
                    .genres(genres)
                    .likesCount(likes)
                    .build();
        });
    }

    @Override
    public List<Genre> getAllGenres() {
        return jdbcTemplate.query(GET_ALL_GENRES_QUERY, (rs, rowNum) -> {
            int id = rs.getInt("genre_id");
            String genreName = rs.getString("genre_name");
            return new Genre(id, genreName);
        });
    }

    @Override
    public Genre getGenreById(int id) {
        SqlRowSet createdRows = jdbcTemplate.queryForRowSet(GET_GENRE_BY_ID, id);
        if (createdRows.next()) {
            String genreName = createdRows.getString("genre_name");
            Genre genre = new Genre(id, genreName);
            log.info(GENRE_FOUND_ID, genre);
            return genre;
        } else {
            log.info(GENRE_NOT_FOUND_ID, id);
            throw new NotFoundException(GENRE_NOT_FOUND_ID_EX + id);
        }
    }

    @Override
    public List<MPA> getAllMPA() {
        return jdbcTemplate.query(GET_ALL_MPA, (rs, rowNum) -> {
            int id = rs.getInt("rating_id");
            String genreName = rs.getString("rating_name");
            return new MPA(id, genreName);
        });
    }

    @Override
    public MPA getMPAById(int id) {
        SqlRowSet createdRows = jdbcTemplate.queryForRowSet(GET_MPA_BY_ID, id);
        if (createdRows.next()) {
            String genreName = createdRows.getString("rating_name");
            MPA mpa = new MPA(id, genreName);
            log.info(MPA_FOUND_ID, mpa);
            return mpa;
        } else {
            log.info(MPA_NOT_FOUND_ID, id);
            throw new NotFoundException(MPA_NOT_FOUND_ID_EX + id);
        }
    }

}