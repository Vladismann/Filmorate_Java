package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
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
    @JsonIgnore
    private final Set<Integer> friends = new HashSet<>();

}