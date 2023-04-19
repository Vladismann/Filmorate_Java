package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Data
public class User implements Resource {

    private int id;
    @NotBlank(message = "Логин не должен быть пустым")
    private String login;
    private String name;
    @NotBlank(message = "Почта не должна быть пустой")
    @Email(message = "Некорректный формат почты")
    private String email;
    @PastOrPresent(message = "Дата не должна быть в будущем")
    private LocalDate birthday;

    public User() {
    }

    public User(String login, String name, String email, LocalDate birthday) {
        this.login = login;
        this.name = name;
        this.email = email;
        this.birthday = birthday;
    }
}