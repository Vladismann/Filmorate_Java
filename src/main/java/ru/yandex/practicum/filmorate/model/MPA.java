package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class MPA {

    int id;

    public MPA() {
    }

    public MPA(int id) {
        this.id = id;
    }
}
