package ru.yandex.practicum.filmorate;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.dao.impl.FilmDbStorageImpl;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class FilmDbStorageTests {

    private final FilmDbStorageImpl filmDbStorage;
    private Film testFilm1;
    private Film testFilm2;
    private static final LocalDate TEST_DATE = LocalDate.of(2000, 8, 20);

    @BeforeEach
    void before() {
        testFilm1 = Film.builder()
                .name("test1")
                .description("test desc1")
                .duration(60)
                .releaseDate(TEST_DATE)
                .mpa(new MPA(1, "G"))
                .genres(List.of(new Genre(2, "Драма")))
                .build();

        testFilm2 = Film.builder()
                .name("test2")
                .description("test desc2")
                .duration(120)
                .releaseDate(TEST_DATE)
                .mpa(new MPA(2, "PG"))
                .genres(List.of(new Genre(1, "Комедия")))
                .build();
    }

    @Test
    public void testCreateFilm() {
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



}
