package ru.yandex.practicum.filmorate.storage;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

@Component
public class LikeDbStorage implements LikeStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public LikeDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void setLike(User user, Film film) {

        String sqlInsert = "INSERT INTO LIKES (USER_ID, FILM_ID) VALUES (?,?);";
        jdbcTemplate.update(sqlInsert, user.getId(), film.getId());
    }

    @Override
    public void deleteLike(User user, Film film) {

        String sqlDelete = "DELETE FROM LIKES WHERE USER_ID = ? AND FILM_ID = ?;";
        jdbcTemplate.update(sqlDelete, user.getId(), film.getId());
    }

}
