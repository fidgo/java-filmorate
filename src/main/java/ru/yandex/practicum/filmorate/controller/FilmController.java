package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NoSuchElementException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.util.idGenerator;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private static final LocalDate MIN_DATE_RELEASE = LocalDate.of(1895, 12, 28);
    private final HashMap<Integer, Film> films = new HashMap<>();
    private final idGenerator idGen = new idGenerator();

    @GetMapping
    public List<Film> getAllFilms() {
        return new ArrayList<Film>(films.values());
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        validateToCreate(film);
        film.setId(idGen.getNewId());
        films.put(film.getId(), film);
        log.info("Добавлен фильм {}", film);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        validateToUpdate(film);
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("Обновлен фильм: {}", film);
            return film;
        } else {
            log.info("Нет такого фильма: {}", film);
            throw new NoSuchElementException("Нет такого фильма!");
        }
    }

    private void validateToCreate(Film film) {
        validate(film);
    }

    private void validateToUpdate(Film film) {
        validate(film);
        validateId(film);
    }

    private void validate(Film film) {

        if (film.getReleaseDate().isBefore(MIN_DATE_RELEASE)) {
            log.info("Дата релиза раньше 28.11.1895 у фильма {}", film);
            throw new ValidationException("Дата релиза — не раньше 28.11.1895!");
        }
    }

    private void validateId(Film film) {
        if ((film.getId() == null) || (film.getId() <= 0)) {
            log.info("Некорректный id у фильма {}", film);
            throw new ValidationException("id фильма равна null ИЛИ меньше или равна нулю");
        }
    }


}
