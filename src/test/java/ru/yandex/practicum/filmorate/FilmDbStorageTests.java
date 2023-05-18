package ru.yandex.practicum.filmorate;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.dao.impl.FilmDbStorageImpl;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class FilmDbStorageTests {

    private final FilmDbStorageImpl filmDbStorage;
    private final UserDbStorage userStorage;
    private Film testFilm1;
    private Film testFilm2;
    private final User testUser1 = User.builder()
            .name("test1")
            .login("testlogin1")
            .email("test1@mail.com")
            .birthday(TEST_DATE)
            .build();
    private static final LocalDate TEST_DATE = LocalDate.of(2000, 8, 20);
    private final Genre testGenre1 = new Genre(2, "Драма");
    private final Genre testGenre2 = new Genre(1, "Комедия");
    private final MPA testMPA1 = new MPA(1, "G");
    private final MPA testMPA2 = new MPA(2, "PG");

    @BeforeEach
    void before() {
        testFilm1 = Film.builder()
                .name("test1")
                .description("test desc1")
                .duration(60)
                .releaseDate(TEST_DATE)
                .mpa(testMPA1)
                .genres(new LinkedHashSet<>(List.of(testGenre1)))
                .build();

        testFilm2 = Film.builder()
                .name("test2")
                .description("test desc2")
                .duration(120)
                .releaseDate(TEST_DATE)
                .mpa(testMPA2)
                .genres(new LinkedHashSet<>(List.of(testGenre2)))
                .build();
    }

    @Test
    public void createFilm() {
        filmDbStorage.createFilm(testFilm1);
        assertEquals(testFilm1, filmDbStorage.getFilmById(1));
    }

    @Test
    public void getFilmById() {
        filmDbStorage.createFilm(testFilm1);
        filmDbStorage.createFilm(testFilm2);
        assertEquals(testFilm1, filmDbStorage.getFilmById(1));
    }

    @Test
    public void cantFindDefunctFilm() {
        assertThrows(NotFoundException.class, () -> filmDbStorage.getFilmById(1));
    }

    @Test
    public void getAllFilms() {
        filmDbStorage.createFilm(testFilm1);
        filmDbStorage.createFilm(testFilm2);
        List<Film> allFilms = filmDbStorage.getAllFilms();
        assertEquals(2, allFilms.size());
        assertEquals(allFilms.get(0), testFilm1);
        assertEquals(allFilms.get(1), testFilm2);
    }

    @Test
    public void getAllEmpty() {
        List<Film> allFilms = filmDbStorage.getAllFilms();
        assertEquals(0, allFilms.size());
    }

    @Test
    public void updateFilm() {
        filmDbStorage.createFilm(testFilm1);
        testFilm2.setId(1);
        filmDbStorage.updateFilm(testFilm2);
        assertEquals(testFilm2, filmDbStorage.getFilmById(1));
    }

    @Test
    public void cantUpdateDefunctFilm() {
        filmDbStorage.createFilm(testFilm1);
        testFilm2.setId(2);
        assertThrows(NotFoundException.class, () -> filmDbStorage.updateFilm(testFilm2));
    }

    @Test
    public void filmAddLike() {
        filmDbStorage.createFilm(testFilm2);
        filmDbStorage.createFilm(testFilm1);
        userStorage.createUser(testUser1);
        filmDbStorage.addLikeToFilm(1, 1);
        List<Film> films = filmDbStorage.getPopularFilms(2);
        assertEquals(testFilm2, films.get(0));
    }

    @Test
    public void filmCantAddLikeTwice() {
        filmDbStorage.createFilm(testFilm2);
        filmDbStorage.createFilm(testFilm1);
        userStorage.createUser(testUser1);
        filmDbStorage.addLikeToFilm(1, 1);
        assertThrows(ValidationException.class, () -> filmDbStorage.addLikeToFilm(1, 1));
    }

    @Test
    public void filmDeleteLike() {
        filmDbStorage.createFilm(testFilm2);
        filmDbStorage.createFilm(testFilm1);
        userStorage.createUser(testUser1);
        filmDbStorage.addLikeToFilm(1, 1);
        filmDbStorage.deleteLikeToFilm(1, 1);
        List<Film> films = filmDbStorage.getPopularFilms(2);
        assertEquals(testFilm2, films.get(0));
    }

    @Test
    public void filmCantDeleteLikeTwice() {
        filmDbStorage.createFilm(testFilm2);
        filmDbStorage.createFilm(testFilm1);
        userStorage.createUser(testUser1);
        filmDbStorage.addLikeToFilm(1, 1);
        filmDbStorage.deleteLikeToFilm(1, 1);
        assertThrows(NotFoundException.class, () -> filmDbStorage.deleteLikeToFilm(1, 1));
    }

    @Test
    public void getPopularFilms() {
        filmDbStorage.createFilm(testFilm1);
        filmDbStorage.createFilm(testFilm2);
        userStorage.createUser(testUser1);
        filmDbStorage.addLikeToFilm(2, 1);
        List<Film> films = filmDbStorage.getPopularFilms(2);
        assertEquals(testFilm2, films.get(0));
        assertEquals(testFilm1, films.get(1));
    }

}