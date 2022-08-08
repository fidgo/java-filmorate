package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.idGenerator;

import java.util.*;
import java.util.stream.Collectors;

@Component("inMemoryFilmStorage")
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private final idGenerator idGen = new idGenerator();

    @Override
    public Film get(long id) {
        return films.get(id);
    }

    @Override
    public Film create(Film film) {
        film.setId(idGen.getNewId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        } else {
            film = null;
        }
        return film;
    }

    @Override
    public List<Film> getAll() {
        return new ArrayList<Film>(films.values());
    }

    @Override
    public void setLike(User user, Film film) {
        film.getLikesId().add(user.getId());
    }

    @Override
    public void deleteLike(User user, Film film) {
        film.getLikesId().remove(user.getId());
    }

    @Override
    public List<Film> getPopular(int count) {
        return films.values().stream()
                .sorted(Comparator.comparing(Film::getLikesId, Comparator.comparingInt(Set::size))
                        .reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public List<MPA> getMPA() {
        return null;
    }

    @Override
    public MPA getMPA(int id) {
        return null;
    }

    @Override
    public List<Genre> getGenres() {
        return null;
    }

    @Override
    public Genre getGenres(int id) {
        return null;
    }
}
