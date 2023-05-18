package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.dao.impl.UserDbStorageImpl;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

class UserControllerTest {

    private Validator validator;
    private UserController controller;
    private static final LocalDate TEST_DATE = LocalDate.of(2000, 8, 20);
    private User userWithIncorrectEmail;
    private User userWithIncorrectLogin;
    private User userWithFeatureBirthDate;

    @BeforeEach
    void before() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        UserDbStorage userStorage = new UserDbStorageImpl(jdbcTemplate);
        UserService userService = new UserService(userStorage);
        validator = factory.getValidator();
        controller = new UserController(userService);
        userWithIncorrectEmail = User.builder().login("test").name("test name").email("testmail@").birthday(TEST_DATE).build();
        userWithIncorrectLogin = User.builder().login("").name("test name").email("testmail@").birthday(TEST_DATE).build();
        userWithFeatureBirthDate = User.builder().login("test").name("").email("testmail@").birthday(LocalDate.now().plusDays(1)).build();
    }

    @Test
    void cantCreateUserWithIncorrectEmail() {
        Set<ConstraintViolation<User>> errors = validator.validate(userWithIncorrectEmail);
        Assertions.assertEquals(1, errors.size());
    }

    @Test
    void cantCreateUserWithEmptyEmail() {
        userWithIncorrectEmail.setEmail("");
        Set<ConstraintViolation<User>> errors = validator.validate(userWithIncorrectEmail);
        Assertions.assertEquals(1, errors.size());
    }

    @Test
    void cantCreateUserWithEmptyLogin() {
        Set<ConstraintViolation<User>> errors = validator.validate(userWithIncorrectLogin);
        Assertions.assertTrue(errors.size() >= 1);
    }

    @Test
    void cantCreateUserWithNullLogin() {
        userWithIncorrectLogin.setLogin(null);
        Set<ConstraintViolation<User>> errors = validator.validate(userWithIncorrectLogin);
        Assertions.assertTrue(errors.size() >= 1);
    }

    @Test
    void cantCreateUserWithBlankLogin() {
        userWithIncorrectLogin.setLogin(" ");
        Set<ConstraintViolation<User>> errors = validator.validate(userWithIncorrectLogin);
        Assertions.assertTrue(errors.size() >= 1);
    }

    @Test
    void cantCreateUserWithLoginWithWhiteSpace() {
        userWithIncorrectLogin.setLogin("Te St");
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> controller.create(userWithIncorrectLogin));
        Assertions.assertEquals("Логин не должен содержать пробел: " + userWithIncorrectLogin.getLogin(), exception.getMessage());
    }

    @Test
    void cantCreateUserWithFutureBirthDate() {
        Set<ConstraintViolation<User>> errors = validator.validate(userWithFeatureBirthDate);
        Assertions.assertTrue(errors.size() >= 1);
    }

}