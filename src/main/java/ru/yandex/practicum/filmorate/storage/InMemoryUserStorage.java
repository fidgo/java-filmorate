package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.idGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
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
    public void setFriend(User user, User friends) {
        user.getFriendsId().add(friends.getId());
        friends.getFriendsId().add(user.getId());
    }

    @Override
    public void deleteFriend(User user, User friend) {
        user.getFriendsId().remove(friend.getId());
        friend.getFriendsId().remove(user.getId());
    }
}
