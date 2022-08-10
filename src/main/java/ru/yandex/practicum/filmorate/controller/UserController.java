package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        log.info("get /users/");
        return userService.getAll();
    }

    @GetMapping("/{idUser}")
    public User getUser(@PathVariable long idUser) {
        log.info("get /users/{idUser} by idUser={}", idUser);
        return userService.get(idUser);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable long id) {
        log.info("get /users/{id}/friends by id={}", id);
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable long id, @PathVariable long otherId) {
        log.info("get /users/{id}/friends/common/{otherId} by id={} and otherId={}", id, otherId);
        return userService.getCommonFriends(id, otherId);
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("post /users by user={}", user);
        updateIfEmptyNameByLogin(user);
        return userService.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("put /users by user={}", user);
        validateId(user);
        updateIfEmptyNameByLogin(user);
        return userService.update(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable long id, @PathVariable long friendId) {
        log.info("put /users/{id}/friends/{friendId} by id={} and friendId={}", id, friendId);
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable long id, @PathVariable long friendId) {
        log.info("delete /users/{id}/friends/{friendId} by id={} and friendId={}", id, friendId);
        userService.deleteFriend(id, friendId);
    }


    private void validateId(User user) {
        String mes = null;

        if (user.getId() == null) {
            mes = "id фильма равна null";
        }

        if (mes != null) {
            log.info("Некорректный id у пользователя {}", user);
            throw new ValidationException(mes);
        }
    }

    private void updateIfEmptyNameByLogin(User user) {
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("У пользователя {} измнилась name на логин", user);
        }
    }
}
