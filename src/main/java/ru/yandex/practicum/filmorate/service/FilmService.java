package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NoSuchElementException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Slf4j
@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                       @Qualifier("userDbStorage") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film get(long id) {
        return getFilmIfExistOrThrowException(id);
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public void setLike(long idUser, long idFilm) {
        User user = getUserIfExistOrThrowException(idUser);
        Film film = getFilmIfExistOrThrowException(idFilm);

        filmStorage.setLike(user, film);
    }

    public void deleteLike(long idUser, long idFilm) {
        User user = getUserIfExistOrThrowException(idUser);
        Film film = getFilmIfExistOrThrowException(idFilm);

        filmStorage.deleteLike(user, film);
    }

    public Film update(Film film) {
        getFilmIfExistOrThrowException(film.getId());

        return filmStorage.update(film);
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    private User getUserIfExistOrThrowException(long id) {
        User gotUser = userStorage.get(id);

        if (gotUser == null) {
            throw new NoSuchElementException("Нет пользователя c id=" + id);
        }
        return gotUser;
    }

    private Film getFilmIfExistOrThrowException(long id) {
        Film gotFilm = filmStorage.get(id);

        if (gotFilm == null) {
            throw new NoSuchElementException("Нет фильма c id=" + id);
        }
        return gotFilm;
    }

    public List<Film> getPopular(int count) {
        return filmStorage.getPopular(count);
    }

    public List<MPA> getMpa() {
        return filmStorage.getMPA();
    }

    public MPA getMpa(int id) {
        MPA gotMpa = filmStorage.getMPA(id);

        if (gotMpa == null) {
            throw new NoSuchElementException("Нет MPA c id=" + id);
        }
        return gotMpa;
    }

    public List<Genre> getGenre() {
        return filmStorage.getGenres();
    }

    public Genre getGenre(int id) {
        Genre gotGenre = filmStorage.getGenres(id);

        if (gotGenre == null) {
            throw new NoSuchElementException("Нет Genre c id=" + id);
        }
        return gotGenre;
    }
}
