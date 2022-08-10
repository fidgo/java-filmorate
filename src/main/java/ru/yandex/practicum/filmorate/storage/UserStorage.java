package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    User get(long id);

    User create(User user);

    User update(User user);

    List<User> getAll();

    List<User> getFriends(User user);

    List<User> getCommonFriends(User user, User other);
}
