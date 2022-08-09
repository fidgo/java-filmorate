package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

public interface FriendStorage {
    void setFriend(User user, User friend);

    void deleteFriend(User user, User friend);
}
