package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.MPADbStorage;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;

import static ru.yandex.practicum.filmorate.messages.TechnicalMessages.*;
import static ru.yandex.practicum.filmorate.query.FilmQuery.GET_ALL_MPA;
import static ru.yandex.practicum.filmorate.query.FilmQuery.GET_MPA_BY_ID;

@Component
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MPADbStorageImpl implements MPADbStorage {

    private final JdbcTemplate jdbcTemplate;

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
