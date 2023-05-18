package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.dao.impl.MPADbStorageImpl;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MPAStorageTests {

    private final MPADbStorageImpl mpaDbStorage;
    private final MPA testMPA1 = new MPA(1, "G");
    private final MPA testMPA2 = new MPA(2, "PG");

    @Test
    public void getMPAById() {
        assertEquals(testMPA1, mpaDbStorage.getMPAById(1));
    }

    @Test
    public void cantGetDefunctMPAById() {
        assertThrows(NotFoundException.class, () -> mpaDbStorage.getMPAById(777));
    }

    @Test
    public void getAllMpa() {
        List<MPA> mpa = mpaDbStorage.getAllMPA();
        assertEquals(5, mpa.size());
        assertTrue(mpa.contains(testMPA1));
        assertTrue(mpa.contains(testMPA2));
    }
}