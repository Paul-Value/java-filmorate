package ru.yandex.practicum.filmorate.repository.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    List<User> getAll();

    Optional<User> get(long id);

    User save(User user);

    User update(User user);

    void addFriend(long userId, long friendId);

    void deleteFriend(long userId, long friendId);

    List<User> getFriends(long userId);

    List<User> getCommonFriends(long userId, long otherUserId);

    void checkExistUser(Long id);
}
