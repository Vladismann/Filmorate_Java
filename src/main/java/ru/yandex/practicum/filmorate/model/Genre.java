package ru.yandex.practicum.filmorate.model;


import lombok.Data;

@Data
public class Genre {

    int id;

    public Genre(int id) {
        this.id = id;
    }
}
