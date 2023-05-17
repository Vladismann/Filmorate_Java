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
    public static final String GET_MAX_ID_ERROR = "Ошибка при получении максимального id: ";

    //films
    public static final String FILM_CREATED = "Добавлен фильм: {}";

    public static final String FILM_FOUND_ID = "Фильм найден: {}";
    public static final String FILM_NOT_FOUND_ID = "Фильм с id: {} не найден.";
    public static final String FILM_NOT_FOUND_ID_EX = "Фильм не найден. Id: ";

    public static final String GENRE_FOUND_ID = "Жанр найден: {}";
    public static final String GENRE_NOT_FOUND_ID = "Жанр с id: {} не найден.";
    public static final String GENRE_NOT_FOUND_ID_EX = "Жанр не найден. Id: ";

    public static final String GET_MPA = "Запрос списка mpa: {}";
    public static final String MPA_FOUND_ID = "MPA найден: {}";
    public static final String MPA_NOT_FOUND_ID = "MPA с id: {} не найден.";
    public static final String MPA_NOT_FOUND_ID_EX = "MPA не найден. Id: ";

    public static final String GET_FILM_GENRES = "Запрос списка жанров фильма с id: {}";
    public static final String DELETE_FILM_GENRES = "Старые жанры фильма с id: {} удалены";
    public static final String DELETE_FILM_GENRES_EX = "Невозможно удалить жанры фильма с id: ";
    public static final String FILM_GENRE_ADD_ERROR_EX = "Ошибка при добавлении жанров фильму с id: ";
    public static final String FILM_ADDED_GENRE = "Фильму с id: {} добавлен жанр с id: {}";

    public static final String FILM_CREATION_ERROR = "Ошибка при создании фильма. Фильм: ";
    public static final String FILM_UPDATE_ERROR = "Ошибка при обновлении фильма. Фильм: ";

    public static final String INCORRECT_FILM_DATE = "Отправлен некорректная дата фильма: {}";
    public static final String INCORRECT_FILM_DATE_EX = " указанная дата раньше первого фильма в истории кино";
    public static final String RECEIVED_FILMS = "Получено количество фильмов: {}";
    public static final String RECEIVED_GENRES = "Запрошены все жанры: {}";

    public static final String ADDED_LIKE = "Фильму с id: {} добавлен лайк пользователя с id: {}";
    public static final String ADD_LIKE_ERROR = "Фильму с id: {} невозможно добавить лайк пользователя с id: {}. Уже существует";
    public static final String DELETE_LIKE_ERROR = "Фильму с id: {} невозможно удалить лайк пользователя с id: {}. Не существует.";
    public static final String ADD_LIKE_EX = "Лайк уже существует. Id фильма и пользователя: ";
    public static final String DELETE_LIKE_EX = "Лайк не существует. Id фильма и пользователя: ";
    public static final String DELETED_LIKE = "Фильму с id: {} удален лайк пользователя с id: {}";

    public static final String GET_POPULAR_FILMS = "Запрос популярных фильмов в количестве: {}";

    //users
    public static final String LOGIN_WITH_WHITESPACE = "Отправлен логин с пробелом: {}";
    public static final String LOGIN_WITH_WHITESPACE_EX = "Логин не должен содержать пробел: ";
    public static final String USER_HAS_EMPTY_NAME = "Пустое имя заменено на логин пользователя: {}";
    public static final String USER_CREATED = "Создан пользователь: {}";

    public static final String USER_NOT_FOUND_ID_EX = "Пользователь не найден. Id: ";
    public static final String USER_FOUND_ID = "Пользователь найден: {}";
    public static final String USER_NOT_FOUND_ID = "Пользователь с id {} не найден.";

    public static final String USER_CREATION_ERROR = "Ошибка при создании пользователя. Пользователь: {}";
    public static final String USER_UPDATE_ERROR = "Ошибка при обновлении пользователя. Пользователь: {}";

    public static final String RECEIVED_USERS = "Получено текущее количество пользователей: {}";

    public static final String ADDED_FRIEND = "Пользователю с id: {} добавлен друг с id: {}";
    public static final String CANT_ADD_FRIEND = "Ошибка при добавлении пользователю с id: {} друга с id: {}. Уже был добавлен.";
    public static final String CANT_ADD_FRIEND_EX = "Дружба пользователей уже существует. Id пользователей: ";
    public static final String CANT_DELETE_FRIEND = "Ошибка при удалении друга пользователю с id: {} друга с id: {}. Не найдено.";
    public static final String CANT_DELETE_FRIEND_EX = "Дружба не найдена. Id пользователей:";
    public static final String DELETED_FRIEND = "Пользователь с id: {} удалил друга с id: {}";

    public static final String GET_USER_FRIENDS = "Запрос списка друзей пользователя с id: {}";
    public static final String GET_COMMON_FRIENDS = "Запрос общих друзей пользователя с id: {} и id: {}";
}