package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class MPA {

    private int id;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String name;

    public MPA() {
    }

    public MPA(int id) {
        this.id = id;
    }

    public MPA(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
