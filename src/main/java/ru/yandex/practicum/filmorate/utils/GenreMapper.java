package ru.yandex.practicum.filmorate.utils;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GenreMapper {

    public static Genre genreListMapper(ResultSet set) throws SQLException {
        int id = set.getInt("genre_id");
        String genreName = set.getString("genre_name");
        return new Genre(id, genreName);
    }

    public static Genre genreListMapper(SqlRowSet set) {
        int id = set.getInt("genre_id");
        String genreName = set.getString("genre_name");
        return new Genre(id, genreName);
    }
}
