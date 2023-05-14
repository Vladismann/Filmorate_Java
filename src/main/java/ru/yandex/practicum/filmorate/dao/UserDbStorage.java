package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserDbStorage {

    User getUserByLogin(String login);

    User getUserById(int id);

    List<User> getAllUsers();

    User createUser(User user);

    User updateUser(User user);
}
