package ru.yandex.practicum.filmorate.query;

public class FilmQuery {

    public static final String CREATE_FILM = "INSERT INTO films " +
            "(name, description, release_date, duration, rating_id) " +
            "VALUES (?, ?, ?, ?, ?)";
    public static final String ADD_FILM_GENRES = "INSERT INTO film_genres " +
            "(film_id, genre_id) " +
            "VALUES (?, ?)";
    public static final String GET_FILM_LAST_ID = "SELECT MAX(film_id) AS id FROM films";
    public static final String GET_FILM_BY_ID = "SELECT * FROM films f " +
            "INNER JOIN rating_mpa r ON f.rating_id = r.rating_id " +
            "WHERE f.film_id = ?";
    public static final String GET_ALL_FILMS = "SELECT * FROM films f " +
            "INNER JOIN rating_mpa r ON f.rating_id = r.rating_id ";

    public static String getFilmGenresIdsQuery(int filmId) {
        return "SELECT fg.genre_id, g.genre_name FROM film_genres fg " +
                "INNER JOIN genres g ON fg.genre_id = g.genre_id " +
                "WHERE fg.film_id = " + filmId;
    }

    public static final String DELETE_OLD_GENRES = "DELETE FROM film_genres WHERE film_id = ?";
    public static final String UPDATE_FILM = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, rating_id = ? WHERE film_id = ?";
    public static final String ADD_LIKE = "INSERT INTO film_likes " +
            "(film_id, user_id) " +
            "VALUES (?, ?)";
    public static final String CHECK_LIKE = "SELECT * FROM film_likes WHERE film_id = ? AND user_id = ?";
    public static final String DELETE_LIKE = "DELETE FROM film_likes WHERE film_id = ? AND user_id = ?";

    public static String getPopularFilmsQuery(int count) {
        return "SELECT f.*, r.*, COUNT(fl.film_id) as likes " +
                "FROM films f " +
                "INNER JOIN rating_mpa r ON f.rating_id = r.rating_id " +
                "LEFT JOIN film_likes fl ON f.film_id = fl.film_id " +
                "GROUP BY f.film_id " +
                "ORDER BY likes DESC LIMIT " + count;
    }

    public static final String GET_ALL_GENRES_QUERY = "SELECT * FROM genres";
    public static final String GET_GENRE_BY_ID = "SELECT * FROM genres WHERE genre_id = ?";
    public static final String GET_ALL_MPA = "SELECT * FROM rating_mpa";
    public static final String GET_MPA_BY_ID = "SELECT * FROM rating_mpa WHERE rating_id = ?";
}