package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmDbStorage {

    List<Film> getAllFilms();

    Film createFilm(Film film);

    Film getFilmById(int id);
}
