package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.dao.FilmDbStorage;

public interface FilmStorage extends FilmDbStorage {
    void addLikeToFilm(int filmId, int userId);

    void deleteLikeToFilm(int filmId, int userId);
}