package ru.yandex.practicum.filmorate.query;

public class FilmQuery {

    public static final String CREATE_FILM = "INSERT INTO films " +
            "(name, description, release_date, duration, rating_id) " +
            "VALUES (?, ?, ?, ?, ?)";
    public static final String ADD_FILM_GENRES = "INSERT INTO film_genres " +
            "(film_id, genre_id) " +
            "VALUES (?, ?)";
    public static final String GET_FILM_LAST_ID = "SELECT MAX(film_id) AS id FROM films";
    public static final String GET_FILM_BY_NAME = "SELECT * FROM films f " +
            "INNER JOIN rating_mpa r ON f.rating_id = r.rating_id " +
            "WHERE f.film_id = ?";
    public static String getFilmGenresIdsQuery(int filmId) {
        return "SELECT fg.genre_id, g.genre_name FROM film_genres fg "  +
                "INNER JOIN genres g ON fg.genre_id = g.genre_id " +
                "WHERE fg.film_id = " + filmId;
    }

}
