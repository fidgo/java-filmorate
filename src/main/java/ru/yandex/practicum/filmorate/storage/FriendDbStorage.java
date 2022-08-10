package ru.yandex.practicum.filmorate.storage;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

@Component
public class FriendDbStorage implements FriendStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FriendDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void setFriend(User user, User friend) {

        String sqlInsert = "INSERT INTO FRIENDSHIP (USER_ID , FRIEND_ID) VALUES (?,?);";
        jdbcTemplate.update(sqlInsert, user.getId(), friend.getId());
    }

    @Override
    public void deleteFriend(User user, User friend) {

        String sqlDelete = "DELETE FROM FRIENDSHIP WHERE USER_ID = ? AND FRIEND_ID = ?;";
        jdbcTemplate.update(sqlDelete, user.getId(), friend.getId());
    }
}
