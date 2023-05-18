package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;

public interface MPADbStorage {
    List<MPA> getAllMPA();

    MPA getMPAById(int id);
}
