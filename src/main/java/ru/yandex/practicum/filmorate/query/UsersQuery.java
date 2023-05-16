package ru.yandex.practicum.filmorate.query;

public class UsersQuery {

    public static final String CREATE_USER = "INSERT INTO users " +
            "(login, name, email, birth_date) " +
            "VALUES (?, ?, ?, ?)";

    public static final String GET_USER_LAST_ID = "SELECT MAX(user_id) AS id FROM users";
    public static final String GET_USER_BY_ID = "SELECT * FROM users WHERE user_id = ?";
    public static final String UPDATE_USER = "UPDATE users SET login = ?, name = ?, email = ?, birth_date = ? WHERE user_id = ?";
    public static final String GET_ALL_USERS = "SELECT * FROM users";
    public static final String CHECK_FRIENDSHIP = "SELECT * FROM user_friends WHERE user_id = ? AND friend_id = ? AND confirmed = TRUE";
    public static final String ADD_FRIEND = "INSERT INTO user_friends " +
            "(user_id, friend_id) " +
            "VALUES (?, ?)";
    public static final String DELETE_FRIEND = "DELETE FROM USER_FRIENDS WHERE USER_ID = ? AND FRIEND_ID = ?";

    public static String getUserFriendsQuery(int id) {
        return "SELECT * FROM USERS WHERE user_id IN (SELECT friend_id FROM USER_FRIENDS WHERE USER_ID = " + id + " AND confirmed = TRUE)";
    }


}
