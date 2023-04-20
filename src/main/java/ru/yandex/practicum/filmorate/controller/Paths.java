package ru.yandex.practicum.filmorate.controller;

public final class Paths {
    public static final String FILMS_PATH = "/films";
    public static final String USERS_PATH = "/users";
    public static final String GET_BY_ID = "/{id}";
    public static final String UPDATE_FRIEND_PATH = "/{id}/friends/{friendId}";
    public static final String GET_USER_FRIENDS = "{id}/friends";
    public static final String GET_COMMON_FRIENDS = "{id}/friends/common/{otherId}";

}
