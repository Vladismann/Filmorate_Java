package ru.yandex.practicum.filmorate.query;

public class UsersQuery {

    public static final String CREATE_USER = "INSERT INTO users " +
            "(login, name, email, birth_date) " +
            "VALUES (?, ?, ?, ?)";

    public static final String GET_USER_BY_LOGIN = "SELECT * FROM users WHERE login = ?";
    public static final String GET_USER_BY_ID = "SELECT * FROM users WHERE user_id = ?";
    public static final String UPDATE_USER = "UPDATE users SET login = ?, name = ?, email = ?, birth_date = ? WHERE user_id = ?";
    public static final String GET_ALL_USERS  = "SELECT * FROM users";


}
