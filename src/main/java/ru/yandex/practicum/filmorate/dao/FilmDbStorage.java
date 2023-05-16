package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface FilmDbStorage {

    List<Genre> getFilmGenres(int filmId);

    Film getFilmByName(String name);

    Film createFilm(Film film);
}
