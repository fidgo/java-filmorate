package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FilmStorage {

    Film get(long id);

    Film create(Film film);

    Film update(Film film);

    List<Film> getAll();

    void setLike(User user, Film film);

    void deleteLike(User user, Film film);
}
