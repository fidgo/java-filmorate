package ru.yandex.practicum.filmorate.storage;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NoSuchElementException;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;

@Component
public class MPADbStorage implements MPAStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MPADbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<MPA> getMPA() {
        String select = "SELECT * FROM MPAS;";
        List<MPA> allMpa = jdbcTemplate.query(select, (rs, rowNum) ->
                (new MPA(rs.getInt("ID"), rs.getString("NAME"))));

        return allMpa;
    }

    @Override
    public MPA getMPA(int id) {
        String select = "SELECT * FROM MPAS WHERE ID = ?;";
        List<MPA> allMpa = jdbcTemplate.query(select, (rs, rowNum) ->
                (new MPA(rs.getInt("ID"), rs.getString("NAME"))), id);

        if (allMpa.isEmpty()) {
            throw new NoSuchElementException("Нет MPA c id=" + id);
        }

        return allMpa.get(0);
    }
}
