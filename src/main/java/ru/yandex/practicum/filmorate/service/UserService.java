package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    public User save(User user) {
        log.info("==>POST /users {}", user);
        User newUser = userStorage.save(user);
        log.info("POST /user <== {}", newUser);
        return newUser;
    }

    public User update(User user) {
        userStorage.checkExistUser(user.getId());
        log.info("==>PUT /users {}", user);
        User updatedUser = userStorage.update(user);
        log.info("PUT /users <== {}", updatedUser);
        return user;
    }

    public List<User> getAll() {
        log.info("GET /users");
        return userStorage.getAll();
    }

    public User get(int id) {
        log.info("GET /users/id");
        return userStorage.get(id)
                .orElseThrow(() -> new NotFoundException("The user with the ID was not found " + id));
    }

    public void addFriend(long userId, long friendId) {
        userStorage.checkExistUser(userId);
        userStorage.checkExistUser(friendId);
        log.info("Add friend with id={} for User with id={}", friendId, userId);
        userStorage.addFriend(userId, friendId);

    }

    public void deleteFriend(long userId, long friendId) {
        userStorage.checkExistUser(userId);
        userStorage.checkExistUser(friendId);
        log.info("Delete friend with id={} from User with id={}", friendId, userId);
        userStorage.deleteFriend(userId, friendId);
    }

    public List<User> getCommonFriends(long userId, long otherUserId) {
      userStorage.checkExistUser(userId);
      userStorage.checkExistUser(otherUserId);
        log.info("==> GET /common friends for users with id={} and {} <==", userId, otherUserId);
      return userStorage.getCommonFriends(userId, otherUserId);
    }

    public List<User> getFriends(Long userId) {
        userStorage.checkExistUser(userId);
        log.info("==> GET /friends for user with id={}", userId);
        return userStorage.getFriends(userId);
    }
}
