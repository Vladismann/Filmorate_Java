package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

public interface UserDbStorage {

    User getUserByLogin(String login);

    User createUser(User user);
}
