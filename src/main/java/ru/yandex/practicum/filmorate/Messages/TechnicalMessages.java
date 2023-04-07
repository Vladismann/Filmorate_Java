package ru.yandex.practicum.filmorate.Messages;

public final class TechnicalMessages {

    //films
    public static final String INCORRECT_FILM_DATE = "Отправлен некорректная дата фильма: {}";
    public static final String INCORRECT_FILM_DATE_EX = " указанная дата раньше первого фильма в истории кино";
    public static final String FILM_NOT_FOUND = "Отправлен некорректный id фильма: {}";
    public static final String FILM_NOT_FOUND_EX = "Фильм не найден";
    public static final String RECEIVED_FILMS = "Получено количество фильмов: {}";
    public static final String ADDED_FILM = "Фильм добавлен: {}";
    public static final String UPDATED_FILM = "Фильм обновлен: {}";
    //users
    public static final String LOGIN_WITH_WHITESPACE = "Отправлен логин с пробелом: {}";
    public static final String LOGIN_WITH_WHITESPACE_EX = "Логин не должен содержать пробел";
    public static final String USER_HAS_EMPTY_NAME = "Пустое имя заменено на логин пользователя: {}";
    public static final String RECEIVED_USERS = "Получено текущее количество пользователей: {}";
    public static final String ADDED_USER = "Пользователь добавлен: {}";
    public static final String USER_NOT_FOUND = "Отправлен несуществующий id пользователя: {}";
    public static final String USER_NOT_FOUND_EX = "Пользователь не найден";
    public static final String UPDATED_USER = "Пользователь обновлен: {}";

}