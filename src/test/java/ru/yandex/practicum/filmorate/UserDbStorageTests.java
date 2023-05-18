package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserDbStorageTests {

    private final UserDbStorage userStorage;
    private static final LocalDate TEST_DATE = LocalDate.of(2000, 8, 20);
    private User testUser1;
    private User testUser2;
    private User testUser3;

    @BeforeEach
    void before() {
        testUser1 = User.builder()
                .name("test1")
                .login("testlogin1")
                .email("test1@mail.com")
                .birthday(TEST_DATE)
                .build();

        testUser2 = User.builder()
                .name("test2")
                .login("testlogin2")
                .email("test2@mail.com")
                .birthday(TEST_DATE)
                .build();

        testUser3 = User.builder()
                .name("test3")
                .login("testlogin3")
                .email("test3@mail.com")
                .birthday(TEST_DATE)
                .build();
    }

    @Test
    public void testCreateUser() {
        userStorage.createUser(testUser1);
        assertEquals(testUser1, userStorage.getUserById(1));
    }

    @Test
    public void getUserById() {
        userStorage.createUser(testUser1);
        userStorage.createUser(testUser2);
        assertEquals(testUser1, userStorage.getUserById(1));
    }

    @Test
    public void cantFindDefunctUser() {
        assertThrows(NotFoundException.class, () -> userStorage.getUserById(1));
    }

    @Test
    public void getAllUsers() {
        userStorage.createUser(testUser1);
        userStorage.createUser(testUser2);
        List<User> allUsers = userStorage.getAllUsers();
        assertEquals(2, allUsers.size());
        assertEquals(allUsers.get(0), testUser1);
        assertEquals(allUsers.get(1), testUser2);
    }

    @Test
    public void getAllEmpty() {
        List<User> allUsers = userStorage.getAllUsers();
        assertEquals(0, allUsers.size());
    }

    @Test
    public void updateUser() {
        userStorage.createUser(testUser1);
        testUser2.setId(1);
        userStorage.updateUser(testUser2);
        assertEquals(testUser2, userStorage.getUserById(1));
    }

    @Test
    public void cantUpdateDefunctUser() {
        userStorage.createUser(testUser1);
        testUser2.setId(2);
        assertThrows(NotFoundException.class, () -> userStorage.updateUser(testUser2));
    }

    @Test
    public void userAddFriend() {
        userStorage.createUser(testUser1);
        userStorage.createUser(testUser2);
        userStorage.addUserFriend(1, 2);
        List<User> test1Friends = userStorage.getUserFriends(1);
        assertEquals(1, test1Friends.size());
        assertEquals(testUser2, test1Friends.get(0));
        List<User> test2Friends = userStorage.getUserFriends(2);
        assertEquals(0, test2Friends.size());
    }

    @Test
    public void userCantAddFriendTwice() {
        userStorage.createUser(testUser1);
        userStorage.createUser(testUser2);
        userStorage.addUserFriend(1, 2);
        assertThrows(ValidationException.class, () -> userStorage.addUserFriend(1, 2));
    }

    @Test
    public void userDeleteFriend() {
        userStorage.createUser(testUser1);
        userStorage.createUser(testUser2);
        userStorage.addUserFriend(1, 2);
        userStorage.deleteUserFriend(1, 2);
        List<User> test1Friends = userStorage.getUserFriends(1);
        assertEquals(0, test1Friends.size());
    }

    @Test
    public void userCantDeleteFriendTwice() {
        userStorage.createUser(testUser1);
        userStorage.createUser(testUser2);
        userStorage.addUserFriend(1, 2);
        userStorage.deleteUserFriend(1, 2);
        assertThrows(NotFoundException.class, () -> userStorage.deleteUserFriend(1, 2));
    }

    @Test
    public void userCommonFriends() {
        userStorage.createUser(testUser1);
        userStorage.createUser(testUser2);
        userStorage.createUser(testUser3);
        userStorage.addUserFriend(1, 3);
        userStorage.addUserFriend(2, 3);
        List<User> commonFriends = userStorage.getCommonFriends(1, 2);
        assertEquals(1, commonFriends.size());
        assertEquals(testUser3, commonFriends.get(0));
    }

}
