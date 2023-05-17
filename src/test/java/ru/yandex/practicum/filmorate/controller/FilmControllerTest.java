package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.dao.impl.FilmDbStorageImpl;
import ru.yandex.practicum.filmorate.dao.impl.UserDbStorageImpl;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Random;
import java.util.Set;


class FilmControllerTest {

    private Validator validator;
    private FilmController controller;
    private static final LocalDate TEST_DATE = LocalDate.of(2000, 8, 20);
    private final LocalDate dateBeforeCinemaBirthDate = LocalDate.of(1895, 12, 27);
    private Film filmWithEmptyName;
    private Film filmWithIncorrectDescription;
    private Film filmWithIncorrectRealiseDate;
    private Film filmWithIncorrectDuration;
    private static final int INCORRECT_DESCRIPTION_SIZE = 201;
    private final Random random = new Random();
    private final int over201 = random.nextInt(101) + INCORRECT_DESCRIPTION_SIZE;
    private final int less200 = random.nextInt(200);
    //private final long daysFromCinemaBirthToTodayDate = ChronoUnit.DAYS.between(CINEMA_BIRTHDAY, LocalDate.now());

    @BeforeEach
    void before() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        UserStorage userStorage = new UserDbStorageImpl(jdbcTemplate);
        FilmStorage filmStorage = new FilmDbStorageImpl(jdbcTemplate, userStorage);
        FilmService filmService = new FilmService(filmStorage);
        validator = factory.getValidator();
        controller = new FilmController(filmService);
        filmWithEmptyName = Film.builder().name("").description("test").releaseDate(TEST_DATE).duration(90).build();
        filmWithIncorrectDescription = Film.builder().description("T".repeat(INCORRECT_DESCRIPTION_SIZE)).name("test").releaseDate(TEST_DATE).duration(90).build();
        filmWithIncorrectRealiseDate = Film.builder().name("test").description("test").releaseDate(dateBeforeCinemaBirthDate).duration(90).build();
        filmWithIncorrectDuration = Film.builder().name("test").description("test").releaseDate(TEST_DATE).duration(-1).build();
    }

    @Test
    void cantCreateFilmWithEmptyName() {
        Set<ConstraintViolation<Film>> errors = validator.validate(filmWithEmptyName);
        Assertions.assertEquals(1, errors.size());
    }

    @Test
    void cantCreateFilmWithNullName() {
        filmWithEmptyName.setName(null);
        Set<ConstraintViolation<Film>> errors = validator.validate(filmWithEmptyName);
        Assertions.assertEquals(1, errors.size());
    }

    @Test
    void cantCreateFilmWithBlankName() {
        filmWithEmptyName.setName(" ");
        Set<ConstraintViolation<Film>> errors = validator.validate(filmWithEmptyName);
        Assertions.assertEquals(1, errors.size());
    }

    @Test
    void cantCreateFilmWith201CharsInDescription() {
        Assertions.assertEquals(INCORRECT_DESCRIPTION_SIZE, filmWithIncorrectDescription.getDescription().length());
        Set<ConstraintViolation<Film>> errors = validator.validate(filmWithIncorrectDescription);
        Assertions.assertEquals(1, errors.size());
        System.out.println(errors);
    }

    @Test
    void cantCreateFilmWithOver201CharsInDescription() {
        filmWithIncorrectDescription.setDescription("T".repeat(over201));
        Assertions.assertTrue(INCORRECT_DESCRIPTION_SIZE < filmWithIncorrectDescription.getDescription().length());
        Set<ConstraintViolation<Film>> errors = validator.validate(filmWithIncorrectDescription);
        Assertions.assertEquals(1, errors.size());
        System.out.println(errors);
    }

    @Test
    void canCreateFilmWith200CharsInDescription() {
        filmWithIncorrectDescription.setDescription("T".repeat(200));
        Assertions.assertEquals(200, filmWithIncorrectDescription.getDescription().length());
        Set<ConstraintViolation<Film>> errors = validator.validate(filmWithIncorrectDescription);
        Assertions.assertEquals(0, errors.size());
        System.out.println(errors);
    }

    @Test
    void canCreateFilmWithLessThan200CharsInDescription() {
        filmWithIncorrectDescription.setDescription("T".repeat(less200));
        Assertions.assertTrue(200 > filmWithIncorrectDescription.getDescription().length());
        Set<ConstraintViolation<Film>> errors = validator.validate(filmWithIncorrectDescription);
        Assertions.assertEquals(0, errors.size());
        System.out.println(errors);
    }

    @Test
    void cantCreateFilmBeforeCinemaBirthDate() {
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> controller.create(filmWithIncorrectRealiseDate));
        Assertions.assertEquals((filmWithIncorrectRealiseDate.getReleaseDate() + " указанная дата раньше первого фильма в истории кино"), exception.getMessage());
    }

    @Test
    void cantCreateFilmWithNegativeDuration() {
        Set<ConstraintViolation<Film>> errors = validator.validate(filmWithIncorrectDuration);
        Assertions.assertEquals(1, errors.size());
    }

    @Test
    void cantCreateFilmWithZeroDuration() {
        filmWithIncorrectDuration.setDuration(0);
        Set<ConstraintViolation<Film>> errors = validator.validate(filmWithIncorrectDuration);
        Assertions.assertEquals(1, errors.size());
    }

}