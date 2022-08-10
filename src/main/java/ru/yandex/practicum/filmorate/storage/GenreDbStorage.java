package ru.yandex.practicum.filmorate.storage;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NoSuchElementException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Comparator;
import java.util.List;

@Component
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> getGenres() {

        String select = "SELECT * FROM GENRES;";
        List<Genre> allGenres = jdbcTemplate.query(select, (rs, rowNum) ->
                (new Genre(rs.getInt("ID"), rs.getString("NAME"))));

        allGenres.sort(Comparator.comparingInt(Genre::getId));

        return allGenres;

    }

    @Override
    public Genre getGenres(int id) {
        String select = "SELECT * FROM GENRES WHERE ID = ?;";
        List<Genre> allGenres = jdbcTemplate.query(select, (rs, rowNum) ->
                (new Genre(rs.getInt("ID"), rs.getString("NAME"))), id);

        if (allGenres.isEmpty()) {
            throw new NoSuchElementException("Нет Genre c id=" + id);
        }

        return allGenres.get(0);
    }
}
