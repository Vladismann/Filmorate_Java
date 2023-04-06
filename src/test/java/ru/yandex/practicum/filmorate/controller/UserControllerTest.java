package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Set;

class UserControllerTest {

    private Validator validator;
    private UserController controller;
    private static final LocalDate TEST_DATE = LocalDate.of(2000, 8, 20);
    private User userWithIncorrectEmail;
    private User userWithIncorrectLogin;
    private User userWithIncorrectEmptyName;
    private User userWithFeatureBirthDate;

    @BeforeEach
    void before() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        controller = new UserController();
        userWithIncorrectEmail = new User("test", "test name", "testmail@", TEST_DATE);
        userWithIncorrectLogin = new User("", "test name", "test@mail.ru", TEST_DATE);
        userWithIncorrectEmptyName = new User("testReplaceNameByLogin", "", "test@mail.ru", TEST_DATE);
        userWithFeatureBirthDate = new User("test", "test name", "test@mail.ru", LocalDate.now().plusDays(1));
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
        Assertions.assertEquals(1, errors.size());
    }

    @Test
    void cantCreateUserWithNullLogin() {
        userWithIncorrectLogin.setLogin(null);
        Set<ConstraintViolation<User>> errors = validator.validate(userWithIncorrectLogin);
        Assertions.assertEquals(1, errors.size());
    }

    @Test
    void cantCreateUserWithBlankLogin() {
        userWithIncorrectLogin.setLogin(" ");
        Set<ConstraintViolation<User>> errors = validator.validate(userWithIncorrectLogin);
        Assertions.assertEquals(1, errors.size());
    }

    @Test
    void cantCreateUserWithLoginWithWhiteSpace() {
        userWithIncorrectLogin.setLogin("Te St");
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> controller.createUser(userWithIncorrectLogin));
        Assertions.assertEquals("Логин не должен содержать пробел", exception.getMessage());
    }

    @Test
    void emptyUserNameReplacingByUserLogin() throws ValidationException {
        controller.createUser(userWithIncorrectEmptyName);
        Assertions.assertEquals("testReplaceNameByLogin", userWithIncorrectEmptyName.getName());
    }

    @Test
    void nullUserNameReplacingByUserLogin() throws ValidationException {
        userWithIncorrectEmptyName.setName(null);
        controller.createUser(userWithIncorrectEmptyName);
        Assertions.assertEquals("testReplaceNameByLogin", userWithIncorrectEmptyName.getName());
    }

    @Test
    void blankUserNameReplacingByUserLogin() throws ValidationException {
        userWithIncorrectEmptyName.setName(" ");
        controller.createUser(userWithIncorrectEmptyName);
        Assertions.assertEquals("testReplaceNameByLogin", userWithIncorrectEmptyName.getName());
    }

    @Test
    void cantCreateUserWithFutureBirthDate() {
        Set<ConstraintViolation<User>> errors = validator.validate(userWithFeatureBirthDate);
        Assertions.assertEquals(1, errors.size());
    }

}