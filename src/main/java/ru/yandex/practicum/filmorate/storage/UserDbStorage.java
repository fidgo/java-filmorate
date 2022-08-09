package ru.yandex.practicum.filmorate.storage;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

@Component("userDbStorage")
@AllArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public User get(long id) {
        String sqlSelect = "SELECT * FROM USERS WHERE ID = ?;";
        List<User> usersDB = jdbcTemplate.query(sqlSelect, (rs, rowNum) -> userRowMapper(rs), id);
        return usersDB.isEmpty() ? null : usersDB.get(0);
    }

    @Override
    public User create(User user) {
        String sqlInsert = "INSERT INTO USERS (NAME, EMAIL, LOGIN, BIRTHDAY) VALUES (?,?,?,?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement prepareStatement = connection.prepareStatement(sqlInsert,
                    Statement.RETURN_GENERATED_KEYS);
            return prepareStatement(user, prepareStatement);
        }, keyHolder);

        String sqlSelectInserted = "SELECT * FROM USERS WHERE ID = ?;";
        List<User> usersDB = jdbcTemplate.query(sqlSelectInserted, (rs, rowNum) -> userRowMapper(rs),
                keyHolder.getKey().longValue());

        return usersDB.isEmpty() ? null : usersDB.get(0);
    }

    @Override
    public User update(User user) {
        String sqlUpdate = "UPDATE USERS SET NAME = ?, EMAIL = ?, LOGIN = ?, BIRTHDAY = ? WHERE ID = ?";
        jdbcTemplate.update(sqlUpdate, user.getName(), user.getEmail(), user.getLogin(), user.getBirthday(),
                user.getId());

        String sqlSelectUpdated = "SELECT * FROM USERS WHERE ID = ?;";
        List<User> usersDB = jdbcTemplate.query(sqlSelectUpdated, (rs, rowNum) -> userRowMapper(rs),
                user.getId());

        return usersDB.isEmpty() ? null : usersDB.get(0);
    }

    @Override
    public List<User> getAll() {
        String sql = "SELECT * FROM USERS";
        List<User> usersDB = jdbcTemplate.query(sql, (rs, rowNum) -> userRowMapper(rs));
        return usersDB;
    }

    @Override
    public List<User> getFriends(User user) {
        String sql = "SELECT * FROM USERS JOIN FRIENDSHIP ON USERS.ID = FRIENDSHIP.FRIEND_ID" +
                " WHERE FRIENDSHIP.USER_ID = ?;";
        List<User> friendsDB = jdbcTemplate.query(sql, (rs, rowNum) -> userRowMapper(rs), user.getId());
        return friendsDB;
    }

    @Override
    public List<User> getCommonFriends(User user, User other) {

        String sql = "SELECT U.ID, U.NAME, U.EMAIL, U.LOGIN, U.BIRTHDAY FROM USERS AS U JOIN FRIENDSHIP AS F ON" +
                " U.ID = F.FRIEND_ID WHERE (F.USER_ID = ? OR F.USER_ID = ?) GROUP BY U.ID HAVING COUNT(*) = 2;";

        List<User> commonFriendsDB = jdbcTemplate.query(sql, (rs, rowNum) -> userRowMapper(rs), user.getId(),
                other.getId());

        return commonFriendsDB;
    }

    private User userRowMapper(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setLogin(rs.getString("login"));
        user.setBirthday(rs.getDate("birthday").toLocalDate());

        return user;
    }

    private PreparedStatement prepareStatement(User user, PreparedStatement ps) throws SQLException {
        ps.setString(1, user.getName());
        ps.setString(2, user.getEmail());
        ps.setString(3, user.getLogin());
        LocalDate birthday = user.getBirthday();
        if (birthday == null) {
            ps.setNull(4, Types.DATE);
        } else {
            ps.setDate(4, Date.valueOf(birthday));
        }

        return ps;
    }
}
