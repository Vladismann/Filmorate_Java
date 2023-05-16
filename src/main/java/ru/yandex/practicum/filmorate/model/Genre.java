package ru.yandex.practicum.filmorate.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class Genre {

    private int id;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String name;

    public Genre() {
    }

    public Genre(int id) {
        this.id = id;
    }

    public Genre(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
