package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NoSuchElementException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Slf4j
@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikeStorage likeStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage, LikeStorage likeStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.likeStorage = likeStorage;
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

        likeStorage.setLike(user, film);
    }

    public void deleteLike(long idUser, long idFilm) {
        User user = getUserIfExistOrThrowException(idUser);
        Film film = getFilmIfExistOrThrowException(idFilm);

        likeStorage.deleteLike(user, film);
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

}
