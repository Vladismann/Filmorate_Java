package ru.yandex.practicum.filmorate.messages;

public final class TechnicalMessages {

    //common
    public static final String RESOURCE_NOT_FOUND = "Отправлен некорректный id ресусрса: {}";
    public static final String RESOURCE_NOT_FOUND_EX = "Ресурс не существует. Id: ";
    public static final String ADDED_RESOURCE = "Ресурс добавлен: {}";
    public static final String UPDATED_RESOURCE = "Ресурс обновлен: {}";
    public static final String RECEIVED_GET = "Получен запрос GET ";
    public static final String RECEIVED_POST = "Получен запрос POST ";
    public static final String RECEIVED_PUT = "Получен запрос PUT ";
    //films
    public static final String INCORRECT_FILM_DATE = "Отправлен некорректная дата фильма: {}";
    public static final String INCORRECT_FILM_DATE_EX = " указанная дата раньше первого фильма в истории кино";
    public static final String RECEIVED_FILMS = "Получено количество фильмов: {}";

    //users
    public static final String LOGIN_WITH_WHITESPACE = "Отправлен логин с пробелом: {}";
    public static final String LOGIN_WITH_WHITESPACE_EX = "Логин не должен содержать пробел: ";
    public static final String USER_HAS_EMPTY_NAME = "Пустое имя заменено на логин пользователя: {}";
    public static final String RECEIVED_USERS = "Получено текущее количество пользователей: {}";
    public static final String FRIEND_NOT_FOUND_EX = "Друг не найден: ";

}