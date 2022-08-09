package ru.yandex.practicum.filmorate.storage;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component("filmDbStorage")
@AllArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film get(long id) {
        String sql = "SELECT * FROM FILMS as F JOIN MPAS AS M ON F.MPA_ID = M.ID WHERE F.ID = ?;";
        List<Film> filmsDB = jdbcTemplate.query(sql, (rs, rowNum) -> filmRowMapper(rs), id);
        loadGenres(filmsDB);

        Film filmById = filmsDB.isEmpty() ? null : filmsDB.get(0);

        return filmById;
    }

    @Override
    public Film create(Film film) {

        String sqlInsert = "INSERT INTO FILMS (NAME, DESCRIPTION, MPA_ID, RELEASE_DATE, DURATION) VALUES (?,?,?,?,?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement prepareStatement = connection.prepareStatement(sqlInsert,
                    Statement.RETURN_GENERATED_KEYS);

            return prepareStatement(film, prepareStatement);
        }, keyHolder);

        final Long idFilm = keyHolder.getKey().longValue();
        film.setId(idFilm);

        saveGenres(film);

        String sqlSelectInserted = "SELECT FILMS.*, MPAS.NAME FROM FILMS JOIN MPAS ON FILMS.MPA_ID = MPAS.ID" +
                " WHERE FILMS.ID = ?;";
        List<Film> filmsDB = jdbcTemplate.query(sqlSelectInserted, (rs, rowNum) -> filmRowMapper(rs),
                idFilm);

        loadGenres(filmsDB);

        return filmsDB.isEmpty() ? null : filmsDB.get(0);
    }

    @Override
    public Film update(Film film) {
        String sqlUpdate = "UPDATE FILMS SET name = ?, description = ?, mpa_id = ?, release_date = ?, duration = ? " +
                "WHERE id = ?";
        jdbcTemplate.update(sqlUpdate, film.getName(), film.getDescription(), film.getMpa().getId(),
                film.getReleaseDate(), film.getDuration(), film.getId());

        saveGenres(film);

        String sqlSelectUpdated = "SELECT FILMS.*, MPAS.NAME FROM FILMS JOIN MPAS ON FILMS.MPA_ID = MPAS.ID " +
                "WHERE FILMS.ID = ?;";
        List<Film> filmsDB = jdbcTemplate.query(sqlSelectUpdated, (rs, rowNum) -> filmRowMapper(rs),
                film.getId());

        loadGenres(filmsDB);

        return filmsDB.isEmpty() ? null : filmsDB.get(0);
    }

    @Override
    public List<Film> getAll() {

        String sql = "SELECT * FROM FILMS JOIN MPAS ON FILMS.MPA_ID = MPAS.ID;";
        List<Film> filmsDB = jdbcTemplate.query(sql, (rs, rowNum) -> filmRowMapper(rs));
        loadGenres(filmsDB);

        return filmsDB;
    }

    @Override
    public List<Film> getPopular(int count) {

        String selectByRating = "SELECT COUNT(*) as RATING, FILMS.*, MPAS.NAME FROM LIKES" +
                " JOIN FILMS ON LIKES.FILM_ID = FILMS.ID JOIN MPAS ON FILMS.MPA_ID = MPAS.ID " +
                "GROUP BY FILMS.ID ORDER BY RATING DESC LIMIT ?;";
        List<Film> popularFilms = jdbcTemplate.query(selectByRating, (rs, rowNum) -> filmRowMapper(rs), count);

        if (popularFilms.isEmpty()) {
            String selectByFilm = "SELECT * FROM FILMS JOIN MPAS ON FILMS.MPA_ID = MPAS.ID LIMIT ?;";
            popularFilms = jdbcTemplate.query(selectByFilm, (rs, rowNum) -> filmRowMapper(rs), count);
        }

        loadGenres(popularFilms);

        return popularFilms;
    }

    private Film filmRowMapper(ResultSet rs) throws SQLException {

        Film film = new Film();
        film.setId(rs.getLong("id"));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        film.setReleaseDate(rs.getDate("release_date").toLocalDate());
        film.setDuration(rs.getInt("duration"));
        film.setMpa(new MPA(rs.getInt("mpa_id"), rs.getString("mpas.name")));

        return film;
    }

    private void saveGenres(Film film) {

        if ((film.getGenres() == null)) {
            return;
        }

        String sqlDeleteGenres = "DELETE FROM GENRES_PER_FILM WHERE FILM_ID = ?;";
        jdbcTemplate.update(sqlDeleteGenres, film.getId());

        Set<Genre> genres = film.getGenres();
        String sqlInsertGenres = "INSERT INTO GENRES_PER_FILM (FILM_ID, GENRES_ID) VALUES (?, ?);";
        for (Genre genre : genres) {
            jdbcTemplate.update(sqlInsertGenres, film.getId(), genre.getId());
        }
    }

    private void loadGenres(List<Film> films) {

        Map<Long, Film> idToFilm = films.stream().collect(Collectors.toMap(Film::getId, film -> film, (a, b) -> b));
        List<Long> ids = films.stream().map(Film::getId).collect(Collectors.toList());
        String sql = String.join(",", Collections.nCopies(ids.size(), "?"));
        sql = String.format("SELECT GP.GENRES_ID, GP.FILM_ID, G.NAME " +
                "FROM GENRES_PER_FILM AS GP JOIN GENRES AS G ON GP.GENRES_ID = G.ID" +
                " WHERE GP.FILM_ID IN (%s)", sql);

        int[] argTypes = new int[ids.size()];
        Arrays.fill(argTypes, Types.BIGINT);

        jdbcTemplate.query(sql, ids.toArray(), argTypes,
                (rs, rowNum) ->
                        idToFilm.get(rs.getLong("GENRES_PER_FILM.FILM_ID")).getGenres()
                                .add(new Genre(rs.getInt("GENRES_PER_FILM.GENRES_ID"),
                                        rs.getString("GENRES.NAME")))
        );
    }

    private PreparedStatement prepareStatement(Film film, PreparedStatement ps) throws SQLException {
        String title = film.getName();
        if (title == null) {
            ps.setNull(1, Types.VARCHAR);
        } else {
            ps.setString(1, title);
        }

        String description = film.getDescription();
        if (description == null) {
            ps.setNull(2, Types.VARCHAR);
        } else {
            ps.setString(2, description);
        }

        MPA mpa = film.getMpa();
        if (mpa == null) {
            ps.setNull(3, Types.INTEGER);
        } else {
            ps.setInt(3, mpa.getId());
        }

        LocalDate releaseDate = film.getReleaseDate();
        if (releaseDate == null) {
            ps.setNull(4, Types.DATE);
        } else {
            ps.setDate(4, Date.valueOf(releaseDate));
        }
        ps.setInt(5, film.getDuration());

        return ps;
    }

}
