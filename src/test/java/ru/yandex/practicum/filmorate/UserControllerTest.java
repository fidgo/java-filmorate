package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.NoSuchElementException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {
    LocalDate ld;
    User user;
    UserController userController;
    List<User> users;

    @BeforeEach
    void beforeEach() {
        ld = LocalDate.of(1999, 5, 7);
        user = new User(null, "elephant@mail.ru", "King3310", "John", ld);
        userController = new UserController();
    }

    @Test
    void createTotallyValidUser() {

        users = userController.getAllUsers();
        assertNotNull(users, "Список не равен null");
        assertEquals(0, users.size(), "Количество элементов списка не равно нулю!");

        userController.create(user);
        users = userController.getAllUsers();
        assertNotNull(users, "Список не равен null");
        assertEquals(1, users.size(), "Количество элементов списка не равно 1!");
        assertEquals(user, users.get(0), "Пользователи НЕ равны");
    }

    @Test
    void createValidUserWithBlankName() {
        user.setName(" ");
        userController.create(user);
        users = userController.getAllUsers();
        assertNotNull(users, "Список не равен null");
        assertEquals(1, users.size(), "Количество элементов списка не равно 1!");

        assertEquals(user.getLogin(), users.get(0).getName(), "Name НЕ стала Login");
    }


    @Test
    void updateValidUser() {
        userController.create(user);

        User toUpdate = new User(user.getId(), "update@mail.ru", "update", "jupdate", ld);
        userController.update(toUpdate);

        users = userController.getAllUsers();
        assertNotNull(users, "Список не равен null");
        assertEquals(1, users.size(), "Количество элементов списка не равно 1!");

        assertEquals(toUpdate, users.get(0), "Не обновился user");
    }

    @Test
    void updateNonExistingUser() {
        userController.create(user);

        User toUpdate = new User(12222, "update@mail.ru", "update", "jupdate", ld);

        assertThrows(NoSuchElementException.class, () -> userController.update(toUpdate));

        users = userController.getAllUsers();
        assertNotNull(users, "Список не равен null");
        assertEquals(1, users.size(), "Количество элементов списка не равно 1!");
        assertNotEquals(toUpdate, users.get(0), "Обновился user");
    }

    @Test
    void updateUserWithIncorrectId() {
        userController.create(user);

        User toUpdate = new User(0, "update@mail.ru", "update", "jupdate", ld);

        assertThrows(ValidationException.class, () -> userController.update(toUpdate));

        users = userController.getAllUsers();
        assertNotNull(users, "Список не равен null");
        assertEquals(1, users.size(), "Количество элементов списка не равно 1!");
        assertNotEquals(toUpdate, users.get(0), "Обновился user");
    }

    @Test
    void updateValidUserWithBlankName() {
        userController.create(user);

        User toUpdate = new User(user.getId(), "update@mail.ru", "update", " ", ld);
        userController.update(toUpdate);

        users = userController.getAllUsers();
        assertNotNull(users, "Список не равен null");
        assertEquals(1, users.size(), "Количество элементов списка не равно 1!");

        assertEquals(toUpdate, users.get(0), "Не обновился user");
        assertEquals(toUpdate.getLogin(), users.get(0).getName());
    }
}
