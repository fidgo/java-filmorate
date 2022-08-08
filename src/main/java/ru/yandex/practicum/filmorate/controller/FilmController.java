package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
//@RequestMapping("/films")
@Slf4j
@AllArgsConstructor
public class FilmController {

    @Autowired
    private FilmService filmService;

    private static final LocalDate MIN_DATE_RELEASE = LocalDate.of(1895, 12, 28);

    @GetMapping("/films")
    public List<Film> getAllFilms() {
        log.info("get /films/");
        return filmService.getAll();
    }

    @GetMapping("/films/{idFilm}")
    public Film getFilm(@PathVariable long idFilm) {
        log.info("get /films/{idFilm} by idFilm={}", idFilm);
        return filmService.get(idFilm);
    }

    @GetMapping("/films/popular")
    public List<Film> getPopular(@RequestParam(defaultValue = "10") int count) {
        log.info("get /films/popular?count={count} by count={}", count);
        return filmService.getPopular(count);
    }

    @GetMapping("/mpa")
    public List<MPA> getMpa() {
        log.info("get /mpa");
        return filmService.getMpa();
    }

    @GetMapping("/mpa/{id}")
    public MPA getMpa(@PathVariable int id) {
        log.info("get /mpa/{id}");
        return filmService.getMpa(id);
    }

    @GetMapping("/genres")
    public List<Genre> getGenres() {
        log.info("get /genres");
        return filmService.getGenre();
    }

    @GetMapping("/genres/{id}")
    public Genre getGenres(@PathVariable int id) {
        log.info("get /genres/{id}");
        return filmService.getGenre(id);
    }

    @PostMapping("/films")
    public Film create(@Valid @RequestBody Film film) {
        log.info("post /films/ by film={}", film);
        validateToCreate(film);
        return filmService.create(film);
    }

    @PutMapping("/films")
    public Film update(@Valid @RequestBody Film film) {
        log.info("put /films/ by film={}", film);
        validateToUpdate(film);
        return filmService.update(film);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void addLike(@PathVariable long id, @PathVariable long userId) {
        log.info("put /films/{id}/like/{userId} by id={} and userId={}", id, userId);
        filmService.setLike(userId, id);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLike(@PathVariable long id, @PathVariable long userId) {
        log.info("delete films/{id}/like/{userId} by id={} and userId={}", id, userId);
        filmService.deleteLike(userId, id);
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
        String mes = null;

        if (film.getId() == null) {
            mes = "id фильма равна null";
        }

        if (mes != null) {
            log.info("Некорректный id у фильма {}", film);
            throw new ValidationException(mes);
        }
    }
}
