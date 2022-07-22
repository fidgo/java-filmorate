package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    User get(long id);

    User create(User user);

    User update(User user);

    List<User> getAll();

    void setFriend(User user, User friends);

    void deleteFriend(User user, User friend);
}
