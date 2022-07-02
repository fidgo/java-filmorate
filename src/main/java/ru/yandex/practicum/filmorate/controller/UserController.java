package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NoSuchElementException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.idGenerator;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController extends AbstractController<User> {

    @Override
    void validateToCreate(User user) {
        updateIfEmptyNameByLogin(user);
    }

    @Override
    void validateToUpdate(User user) {
        validateId(user);
        updateIfEmptyNameByLogin(user);
    }

    private void validateId(User user) {
        if ((user.getId() == null) || (user.getId() <= 0)) {
            log.info("Некорректный id у пользователя {} измнилась name на логин", user);
            throw new ValidationException("id пользователя равна null ИЛИ меньше или равна нулю");
        }
    }

    private void updateIfEmptyNameByLogin(User user) {
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("У пользователя {} измнилась name на логин", user);
        }
    }

    /*
    private HashMap<Integer, User> users = new HashMap<>();
    private final idGenerator idGen = new idGenerator();

    @GetMapping
    public List<User> getAllUsers() {
        return new ArrayList<User>(users.values());
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        updateIfEmptyNameByLogin(user);
        user.setId(idGen.getNewId());
        users.put(user.getId(), user);
        log.info("Добавлен пользователь {}", user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        validateId(user);
        updateIfEmptyNameByLogin(user);
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("Обновлен пользователь: {}", user);
            return user;
        } else {
            log.info("Нет пользователя: {}", user);
            throw new NoSuchElementException("Нет такого пользователя");
        }
    }

    private void validateId(User user) {
        if ((user.getId() == null) || (user.getId() <= 0)) {
            log.info("Некорректный id у пользователя {} измнилась name на логин", user);
            throw new ValidationException("id пользователя равна null ИЛИ меньше или равна нулю");
        }
    }

    private void updateIfEmptyNameByLogin(User user) {
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("У пользователя {} измнилась name на логин", user);
        }
    }

     */
}
