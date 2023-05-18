package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.dao.impl.GenreDbStorageImpl;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class GenreStorageTests {

    private final GenreDbStorageImpl genreDbStorage;
    private final Genre testGenre1 = new Genre(2, "Драма");
    private final Genre testGenre2 = new Genre(1, "Комедия");

    @Test
    public void getGenreById() {
        assertEquals(testGenre2, genreDbStorage.getGenreById(1));
    }

    @Test
    public void getAllGenres() {
        List<Genre> genres = genreDbStorage.getAllGenres();
        assertEquals(6, genres.size());
        assertTrue(genres.contains(testGenre1));
        assertTrue(genres.contains(testGenre2));
    }

    @Test
    public void cantGetDefunctGenreById() {
        assertThrows(NotFoundException.class, () -> genreDbStorage.getGenreById(777));
    }
}
