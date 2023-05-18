package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreDbStorage {
    List<Genre> getAllGenres();

    Genre getGenreById(int id);
}
