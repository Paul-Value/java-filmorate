package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.user.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    public User save(User user) {
        log.info("==>POST /users {}", user);
        User newUser = userRepository.save(user);
        log.info("POST /user <== {}", newUser);
        return newUser;
    }

    public User update(User user) {
        userRepository.checkExistUser(user.getId());
        log.info("==>PUT /users {}", user);
        User updatedUser = userRepository.update(user);
        log.info("PUT /users <== {}", updatedUser);
        return user;
    }

    public List<User> getAll() {
        log.info("GET /users");
        return userRepository.getAll();
    }

    public User get(int id) {
        log.info("GET /users/id");
        return userRepository.get(id)
                .orElseThrow(() -> new NotFoundException("The user with the ID was not found " + id));
    }

    public void addFriend(long userId, long friendId) {
        userRepository.checkExistUser(userId);
        userRepository.checkExistUser(friendId);
        log.info("Add friend with id={} for User with id={}", friendId, userId);
        userRepository.addFriend(userId, friendId);

    }

    public void deleteFriend(long userId, long friendId) {
        userRepository.checkExistUser(userId);
        userRepository.checkExistUser(friendId);
        log.info("Delete friend with id={} from User with id={}", friendId, userId);
        userRepository.deleteFriend(userId, friendId);
    }

    public List<User> getCommonFriends(long userId, long otherUserId) {
      userRepository.checkExistUser(userId);
      userRepository.checkExistUser(otherUserId);
        log.info("==> GET /common friends for users with id={} and {} <==", userId, otherUserId);
      return userRepository.getCommonFriends(userId, otherUserId);
    }

    public List<User> getFriends(Long userId) {
        userRepository.checkExistUser(userId);
        log.info("==> GET /friends for user with id={}", userId);
        return userRepository.getFriends(userId);
    }
}
