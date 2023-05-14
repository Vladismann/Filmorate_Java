package ru.yandex.practicum.filmorate.messages;

public final class TechnicalMessages {

    //common
    public static final String RESOURCE_NOT_FOUND = "Отправлен некорректный id ресурса: {}";
    public static final String RESOURCE_NOT_FOUND_EX = "Ресурс не существует. Id: ";
    public static final String ADDED_RESOURCE = "Ресурс добавлен: {}";
    public static final String UPDATED_RESOURCE = "Ресурс обновлен: {}";
    public static final String RECEIVED_GET = "Получен запрос GET ";
    public static final String RECEIVED_POST = "Получен запрос POST ";
    public static final String RECEIVED_PUT = "Получен запрос PUT ";
    public static final String GETTING_RESOURCE = "Ресурс запрошен: {}";
    //films
    public static final String INCORRECT_FILM_DATE = "Отправлен некорректная дата фильма: {}";
    public static final String INCORRECT_FILM_DATE_EX = " указанная дата раньше первого фильма в истории кино";
    public static final String RECEIVED_FILMS = "Получено количество фильмов: {}";
    public static final String ADDED_LIKE = "Фильму с id: {} добавлен лайк пользователя с id: {}";
    public static final String DELETED_LIKE = "Фильму с id: {} удален лайк пользователя с id: {}";
    public static final String GET_POPULAR_FILMS =  "Запрос популярных фильмов в количестве: {}";
    //users
    public static final String LOGIN_WITH_WHITESPACE = "Отправлен логин с пробелом: {}";
    public static final String LOGIN_WITH_WHITESPACE_EX = "Логин не должен содержать пробел: ";
    public static final String USER_HAS_EMPTY_NAME = "Пустое имя заменено на логин пользователя: {}";
    public static final String USER_CREATED = "Создан пользователь: {}";

    public static final String USER_NOT_FOUND_LOGIN_EX = "Пользователь не найден. Логин: ";
    public static final String USER_FOUND_LOGIN = "Пользователь с логином {} найден: {}";
    public static final String USER_NOT_FOUND_LOGIN = "Пользователь с логином {} не найден.";

    public static final String USER_NOT_FOUND_ID_EX = "Пользователь не найден. Id: ";
    public static final String USER_FOUND_ID= "Пользователь с id {} найден: {}";
    public static final String USER_NOT_FOUND_ID = "Пользователь с id {} не найден.";

    public static final String USER_CREATION_ERROR = "Ошибка при создании пользователя. Пользователь: {}";
    public static final String USER_UPDATE_ERROR = "Ошибка при обновлении пользователя. Пользователь: {}";

    public static final String RECEIVED_USERS = "Получено текущее количество пользователей: {}";
    public static final String FRIEND_NOT_FOUND_EX = "Друг не найден: ";
    public static final String ADDED_FRIEND = "Пользователю с id: {} добавлен друг с id: {}";
    public static final String CANT_ADD_FRIEND = "Ошибка при добавлении пользователю с id: {} друга с id: {}";
    public static final String CANT_ADD_FRIEND_EX = "Ошибка при добавлении пользователей в друзья. Id:" ;
    public static final String DELETED_FRIEND = "Пользователь с id: {} удалил друга с id: {}";
    public static final String GET_USER_FRIENDS = "Запрос списка друзей пользователя с id: {}";
    public static final String GET_COMMON_FRIENDS = "Запрос общих друзей пользователя с id: {} и id: {}";
}