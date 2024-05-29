package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public User save(User user) {
        return userStorage.save(user);
    }

    public User update(User user) {
        userStorage.checkExistUser(user.getId());
        return userStorage.update(user);
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public void addFriend(long userId, long friendId) {
        userStorage.checkExistUser(userId);
        userStorage.checkExistUser(friendId);
        userStorage.addFriend(userId, friendId);
    }

    public void deleteFriend(long userId, long friendId) {
        userStorage.checkExistUser(userId);
        userStorage.checkExistUser(friendId);
        userStorage.deleteFriend(userId, friendId);
    }

    public List<User> getCommonFriends(long userId, long otherUserId) {
      userStorage.checkExistUser(userId);
      userStorage.checkExistUser(otherUserId);
      return userStorage.getCommonFriends(userId, otherUserId);
    }

    public List<User> getFriends(Long userId) {
        userStorage.checkExistUser(userId);
        return userStorage.getFriends(userId);
    }
}
