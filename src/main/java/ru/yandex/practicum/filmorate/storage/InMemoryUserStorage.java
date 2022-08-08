package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.idGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component("inMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {

    private Map<Long, User> users = new HashMap<>();
    private final idGenerator idGen = new idGenerator();

    @Override
    public User get(long id) {
        return users.get(id);
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<User>(users.values());
    }

    @Override
    public User update(User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
        } else {
            user = null;
        }
        return user;
    }

    @Override
    public User create(User user) {
        user.setId(idGen.getNewId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public void setFriend(User user, User friend) {
        user.getFriendsId().add(friend.getId());
        friend.getFriendsId().add(user.getId());
    }

    @Override
    public void deleteFriend(User user, User friend) {
        user.getFriendsId().remove(friend.getId());
        friend.getFriendsId().remove(user.getId());
    }

    @Override
    public List<User> getFriends(User user) {
        return users.get(user.getId()).getFriendsId().stream().map(this::get).collect(Collectors.toList());
    }

    @Override
    public List<User> getCommonFriends(User user, User other) {
        List<User> userFriends = getFriends(user);
        List<User> otherUserFriends = getFriends(other);
        userFriends.retainAll(otherUserFriends);
        return userFriends;
    }
}
