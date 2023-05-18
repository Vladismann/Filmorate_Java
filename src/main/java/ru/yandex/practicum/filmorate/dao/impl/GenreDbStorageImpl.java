package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenreDbStorage;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.utils.GenreMapper;

import java.util.List;

import static ru.yandex.practicum.filmorate.messages.TechnicalMessages.*;
import static ru.yandex.practicum.filmorate.query.FilmQuery.GET_ALL_GENRES_QUERY;
import static ru.yandex.practicum.filmorate.query.FilmQuery.GET_GENRE_BY_ID;

@Component
@Slf4j
@RequiredArgsConstructor
public class GenreDbStorageImpl implements GenreDbStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> getAllGenres() {
        return jdbcTemplate.query(GET_ALL_GENRES_QUERY, (rs, rowNum) -> GenreMapper.genreListMapper(rs));
    }

    @Override
    public Genre getGenreById(int id) {
        SqlRowSet createdRows = jdbcTemplate.queryForRowSet(GET_GENRE_BY_ID, id);
        if (createdRows.next()) {
            Genre genre = GenreMapper.genreListMapper(createdRows);
            log.info(GENRE_FOUND_ID, genre);
            return genre;
        } else {
            log.info(GENRE_NOT_FOUND_ID, id);
            throw new NotFoundException(GENRE_NOT_FOUND_ID_EX + id);
        }
    }
}
