package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.NoSuchElementException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FilmControllerTest {
    private LocalDate ld;
    private Film film;
    private FilmController filmController;
    private List<Film> films;
    private String correctDescription = "While traveling to California to race against The King";

    @BeforeEach
    void beforeEach() {
        ld = LocalDate.of(2005, 5, 7);
        film = new Film(null, "Cars", correctDescription, ld, 117);
        filmController = new FilmController();
    }

    @Test
    void createTotallyValidFilm() {
        films = filmController.getAllFilms();
        assertNotNull(films, "Список не равен null");
        assertEquals(0, films.size(), "Количество элементов списка не равно нулю!");

        filmController.create(film);
        films = filmController.getAllFilms();
        assertNotNull(films, "Список не равен null");
        assertEquals(1, films.size(), "Количество элементов списка не равно 1!");
        assertEquals(film, films.get(0), "Фильмы НЕ равны");
    }

    @Test
    void createValidFilmWithOldestReleaseDate() {
        LocalDate oldestDate = LocalDate.of(1895, 12, 28);
        film.setReleaseDate(oldestDate);
        filmController.create(film);
        films = filmController.getAllFilms();
        assertNotNull(films, "Список не равен null");
        assertEquals(1, films.size(), "Количество элементов списка не равно 1!");
        assertEquals(film, films.get(0), "Фильмы НЕ равны");
    }


    @Test
    void createValidFilmWithReleaseDate1700Year() {
        LocalDate oldDate = LocalDate.of(1700, 1, 5);
        film.setReleaseDate(oldDate);
        assertThrows(ValidationException.class, () -> filmController.create(film));
        films = filmController.getAllFilms();
        assertNotNull(films, "Список не равен null");
        assertEquals(0, films.size(), "Список НЕ пуст!");
    }

    @Test
    void updateValidFilm() {
        filmController.create(film);

        Film toUpdate = new Film(film.getId(), "update@mail.ru", "update", ld, 156);
        filmController.update(toUpdate);

        films = filmController.getAllFilms();
        assertNotNull(films, "Список не равен null");
        assertEquals(1, films.size(), "Количество элементов списка не равно 1!");

        assertEquals(toUpdate, films.get(0), "Не обновился film");
    }

    @Test
    void updateNonExistingFilm() {
        filmController.create(film);

        Film toUpdate = new Film(100, "update@mail.ru", "update", ld, 156);

        assertThrows(NoSuchElementException.class, () -> filmController.update(toUpdate));

        films = filmController.getAllFilms();
        assertNotNull(films, "Список не равен null");
        assertEquals(1, films.size(), "Количество элементов списка не равно 1!");
        assertNotEquals(toUpdate, films.get(0), "Обновился film");
    }

    @Test
    void updateFilmWithIncorrectId() {
        filmController.create(film);

        Film toUpdate = new Film(0, "update@mail.ru", "update", ld, 156);

        assertThrows(ValidationException.class, () -> filmController.update(toUpdate));

        films = filmController.getAllFilms();
        assertNotNull(films, "Список не равен null");
        assertEquals(1, films.size(), "Количество элементов списка не равно 1!");
        assertNotEquals(toUpdate, films.get(0), "Обновился film");
    }

    @Test
    void updateValidFilmWithReleaseDate1700() {
        filmController.create(film);

        LocalDate oldDate = LocalDate.of(1700, 1, 5);
        Film toUpdate = new Film(1, "update@mail.ru", "update", oldDate, 156);
        assertThrows(ValidationException.class, () -> filmController.update(toUpdate));

        films = filmController.getAllFilms();
        assertNotNull(films, "Список не равен null");
        assertEquals(1, films.size(), "Количество элементов списка не равно 1!");

        assertNotEquals(toUpdate, films.get(0), "Обновился film");
    }

}
