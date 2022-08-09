package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class JavaFilmorateApplicationTests {
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;
    private final LikeStorage likeStorage;
    private final FriendStorage friendStorage;
    private final MPAStorage mpaStorage;
    private final GenreStorage genreStorage;
    private static final String FILM_TITANIC_DESCRIPTION = "Titanic is a 1997 American epic romance and disaster film" +
            " directed, written, produced, and co-edited by James Cameron. Incorporating both historical and" +
            " fictionalized aspects.";

    private static User john;
    private static User jack;
    private static User kirk;
    private static Film titanic;
    private static Film cars;
    private static Film kombat;

    private static List<MPA> allMpas;
    private static List<Genre> allGenres;

    @BeforeAll
    public static void beforeAll() {

        john = new User();
        john.setId(1L);
        john.setName("John");
        john.setEmail("john@gmail.com");
        john.setLogin("john_smith");
        john.setBirthday(LocalDate.of(2000, 9, 15));

        jack = new User();
        jack.setId(1L);
        jack.setName("Jack");
        jack.setEmail("jack@gmail.com");
        jack.setLogin("jack_smith");
        jack.setBirthday(LocalDate.of(2001, 3, 12));

        kirk = new User();
        kirk.setId(2L);
        kirk.setName("Kirk");
        kirk.setEmail("kirk@gmail.com");
        kirk.setLogin("kirk_weil");
        kirk.setBirthday(LocalDate.of(2001, 3, 12));

        titanic = new Film();
        titanic.setId(1L);
        titanic.setName("Titanic");
        titanic.setDescription(FILM_TITANIC_DESCRIPTION);
        titanic.setMpa(new MPA(3, "PG-13"));
        titanic.setReleaseDate(LocalDate.of(1997, 12, 19));
        titanic.setDuration(210);

        cars = new Film();
        cars.setId(1L);
        cars.setName("Cars");
        cars.setDescription("new description");
        cars.setMpa(new MPA(1, "G"));
        cars.setReleaseDate(LocalDate.of(1998, 11, 10));
        cars.setDuration(110);

        kombat = new Film();
        kombat.setId(2L);
        kombat.setName("Kombat");
        kombat.setDescription("New film");
        kombat.setMpa(new MPA(1, "G"));
        kombat.setReleaseDate(LocalDate.of(1998, 11, 10));
        kombat.setDuration(110);

        allMpas = new ArrayList<>();
        allMpas.add(new MPA(1, "G"));
        allMpas.add(new MPA(2, "PG"));
        allMpas.add(new MPA(3, "PG-13"));
        allMpas.add(new MPA(4, "R"));
        allMpas.add(new MPA(5, "NC-17"));

        allGenres = new ArrayList<>();
        allGenres.add(new Genre(1, "Комедия"));
        allGenres.add(new Genre(2, "Драма"));
        allGenres.add(new Genre(3, "Мультфильм"));
        allGenres.add(new Genre(4, "Триллер"));
        allGenres.add(new Genre(5, "Документальный"));
        allGenres.add(new Genre(6, "Боевик"));
    }

    @BeforeEach
    public void beforeEach() {
        userStorage.create(john);
        filmStorage.create(titanic);
    }

    @Test
    public void getUserAndFilmById() {

        User gotUser = userStorage.get(1L);
        assertEquals(john, gotUser, "Разные пользователи");

        Film gotFilm = filmStorage.get(1L);
        assertEquals(titanic, gotFilm, "Разные фильмы");
    }

    @Test
    public void getAllUserAndFilm() {

        List<User> gotListUsers = userStorage.getAll();
        assertNotNull(gotListUsers, "Какой-то список пользователей нашелся");
        assertEquals(1, gotListUsers.size(), "Размер списка другой");
        User gotUser = gotListUsers.get(0);
        assertEquals(john, gotUser, "Разные пользователи");


        List<Film> gotListFilms = filmStorage.getAll();
        assertNotNull(gotListFilms, "Какой-то список фильмов нашелся");
        assertEquals(1, gotListFilms.size(), "Размер списка другой");
        Film gotFilm = gotListFilms.get(0);
        assertEquals(titanic, gotFilm, "Разные фильмы");
    }

    @Test
    public void getUsersAndFilmsByWrongId() {

        User gotUser = userStorage.get(1000L);
        assertNull(gotUser, "Какой-то пользователь нашелся");

        Film gotFilm = filmStorage.get(1000L);
        assertNull(gotFilm, "Какой-то фильм нашелся");
    }

    @Test
    public void updateUserAndFilm() {

        User gotUser = userStorage.update(jack);
        assertEquals(jack, gotUser, "Пользователь не обновился");

        Film gotFilm = filmStorage.update(cars);
        assertEquals(cars, gotFilm, "Фильм не обновился");
    }

    @Test
    public void createUserAndFilm() {

        User gotUser = userStorage.create(kirk);
        assertEquals(kirk, gotUser, "Пользователь не создался");

        Film gotFilm = filmStorage.create(kombat);
        assertEquals(kombat, gotFilm, "Фильм не создался");
    }

    @Test
    public void addFriendAndGetFriendAndDeleteFriend() {

        List<User> johnFriends = userStorage.getFriends(john);
        assertNotNull(johnFriends, "Список равен NULL");
        assertEquals(0, johnFriends.size(), "Друзья какие-то есть");

        User addedKirk = userStorage.create(kirk);
        friendStorage.setFriend(john, addedKirk);
        johnFriends = userStorage.getFriends(john);
        assertNotNull(johnFriends, "Список равен NULL");
        assertEquals(1, johnFriends.size(), "Ошиблись с количеством друзей");
        assertEquals(addedKirk, johnFriends.get(0), "John не дружит с Kirk");

        friendStorage.deleteFriend(john, kirk);
        johnFriends = userStorage.getFriends(john);
        assertNotNull(johnFriends, "Список равен NULL");
        assertEquals(0, johnFriends.size(), "Друзья какие-то есть");

    }

    @Test
    public void addFriendsAndGetCommonFriend() {

        User addedKirk = userStorage.create(kirk);
        User addedJack = userStorage.create(jack);
        friendStorage.setFriend(john, addedKirk);
        List<User> comonFriends = userStorage.getCommonFriends(john, addedJack);
        assertNotNull(comonFriends, "Список равен NULL");
        assertEquals(0, comonFriends.size(), "Ошиблись с количеством общих друзей");

        friendStorage.setFriend(addedJack, addedKirk);
        comonFriends = userStorage.getCommonFriends(john, addedJack);
        assertNotNull(comonFriends, "Список равен NULL");
        assertEquals(1, comonFriends.size(), "Ошиблись с количеством общих друзей");
        assertEquals(addedKirk, comonFriends.get(0), "Kirk не общий друг");

        friendStorage.deleteFriend(addedJack, addedKirk);
        comonFriends = userStorage.getCommonFriends(john, addedJack);
        assertNotNull(comonFriends, "Список равен NULL");
        assertEquals(0, comonFriends.size(), "Ошиблись с количеством общих друзей");

    }

    @Test
    public void setAndDeleteLikesAndGetPopular() {
        User addedKirk = userStorage.create(kirk);
        Film addedKombat = filmStorage.create(kombat);
        likeStorage.setLike(john, titanic);
        likeStorage.setLike(addedKirk, titanic);
        List<Film> popularFilms = filmStorage.getPopular(1);
        assertNotNull(popularFilms, "Список равен NULL");
        assertEquals(1, popularFilms.size(), "Ошиблись с количеством популярных фильмов");
        assertEquals(titanic, popularFilms.get(0), "Популярный фильм не титаник");

        User addedJack = userStorage.create(jack);
        likeStorage.setLike(john, addedKombat);
        likeStorage.setLike(addedKirk, addedKombat);
        likeStorage.setLike(addedJack, addedKombat);
        popularFilms = filmStorage.getPopular(1);
        assertNotNull(popularFilms, "Список равен NULL");
        assertEquals(1, popularFilms.size(), "Ошиблись с количеством популярных фильмов");
        assertEquals(addedKombat, popularFilms.get(0), "Популярный фильм не Kombat");

        likeStorage.deleteLike(addedKirk, addedKombat);
        likeStorage.deleteLike(addedJack, addedKombat);
        popularFilms = filmStorage.getPopular(1);
        assertNotNull(popularFilms, "Список равен NULL");
        assertEquals(1, popularFilms.size(), "Ошиблись с количеством популярных фильмов");
        assertEquals(titanic, popularFilms.get(0), "Популярный фильм не титаник");

    }

    @Test
    public void getMpaAndGenresByIdAndAll() {
        MPA gotMpa = mpaStorage.getMPA(1);
        assertNotNull(gotMpa, "MPA равен NULL");
        assertEquals(new MPA(1, "G"), gotMpa, "Не совпали MPA");

        Genre gotGenre = genreStorage.getGenres(1);
        assertNotNull(gotGenre, "Genre равен NULL");
        assertEquals(new Genre(1, "Комедия"), gotGenre, "Не совпали Genre");

        List<MPA> gotMpas = mpaStorage.getMPA();
        assertNotNull(gotMpa, "Список равен NULL");
        assertEquals(5, gotMpas.size(), "Количество рейтингов не совпало с ожидаемым");
        assertThat(gotMpas).hasSameElementsAs(allMpas);

        List<Genre> gotGenres = genreStorage.getGenres();
        assertNotNull(gotGenres, "Список равен NULL");
        assertEquals(6, gotGenres.size(), "Количество жанров не совпало с ожидаемым");
        assertThat(gotGenres).hasSameElementsAs(allGenres);

    }

}
