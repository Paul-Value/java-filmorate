package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private final Map<Long, Set<Long>> usersFriends = new HashMap<>();

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User save(User user) {
        user.setId(getNextId());
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        if (users.get(user.getId()) == null) {
            throw new NotFoundException("Юзер с id:" + user.getId() + "не найден");
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public void addFriend(long userId, long friendId) {
        if (usersFriends.containsKey(userId)) {
            usersFriends.get(userId).add(friendId);
        } else {
            Set<Long> friends = new HashSet<>();
            friends.add(friendId);
            usersFriends.put(userId, friends);
        }
    }

    @Override
    public void deleteFriend(long userId, long friendId) {
        if (!usersFriends.containsKey(userId)) {
            throw new NotFoundException(String.format("Пользователь с id %d не найден", userId));
        }
        usersFriends.get(userId).remove(friendId);
    }

    @Override
    public List<User> getFriends(long userId) {
        if (!usersFriends.containsKey(userId)) {
            return Collections.emptyList();
        }
        return usersFriends.get(userId).stream().map(users::get).toList();
    }

    @Override
    public List<User> getCommonFriends(long userId, long otherUserId) {
        Set<Long> userFriends = usersFriends.getOrDefault(userId, new HashSet<>());
        Set<Long> otherUserFriends = usersFriends.getOrDefault(otherUserId, new HashSet<>());
        userFriends.retainAll(otherUserFriends);
        return otherUserFriends.stream().map(users::get).toList();
    }

    @Override
    public void checkExistUser(Long id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException(String.format("Пользователь с id %d не найден.", id));
        }
    }

    private Long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
