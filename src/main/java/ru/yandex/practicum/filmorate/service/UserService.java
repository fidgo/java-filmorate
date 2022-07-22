package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NoSuchElementException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {

    @Autowired
    private UserStorage userStorage;

    public User get(long id) {
        return getUserIfExistOrThrowException(id);
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public void addFriend(long id, long idFriend) {
        User user = getUserIfExistOrThrowException(id);
        User friend = getUserIfExistOrThrowException(idFriend);

        userStorage.setFriend(user, friend);
    }

    public void deleteFriend(long id, long idFriend) {
        User user = getUserIfExistOrThrowException(id);
        User friend = getUserIfExistOrThrowException(idFriend);

        userStorage.deleteFriend(user, friend);
    }

    public User update(User user) {
        getUserIfExistOrThrowException(user.getId());

        return userStorage.update(user);
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public List<User> getFriends(long id) {
        User userFromId = getUserIfExistOrThrowException(id);

        return userFromId.getFriendsId().stream()
                .map(userStorage::get)
                .collect(Collectors.toList());
    }

    private User getUserIfExistOrThrowException(long id) {
        User gotUser = userStorage.get(id);

        if (gotUser == null) {
            throw new NoSuchElementException("Нет пользователя c id=" + id);
        }
        return gotUser;
    }

    public List<User> getCommonFriends(long id, long otherId) {
        List<User> userFriends = getFriends(id);
        List<User> otherUserFriends = getFriends(otherId);

        userFriends.retainAll(otherUserFriends);
        return userFriends;
    }
}
