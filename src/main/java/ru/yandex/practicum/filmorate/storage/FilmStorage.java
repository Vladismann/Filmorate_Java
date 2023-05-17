package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage extends FilmDbStorage {
    void addLikeToFilm(int filmId, int userId);

    void deleteLikeToFilm(int filmId, int userId);

    List<Film> getPopularFilms(int count);
}