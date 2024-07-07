package ru.yandex.practicum.filmorate.repository.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.sql.ResultSet;

@Repository
@Qualifier("JdbcUserRepository")
@RequiredArgsConstructor
public class JdbcUserRepository implements UserRepository {
    private final NamedParameterJdbcOperations jdbc;
    private final UserRowMapper mapper;

    @Override
    public List<User> getAll() {
        return jdbc.query("SELECT * FROM users", mapper);
    }

    @Override
    public Optional<User> get(long id) {
        String sql = "SELECT * FROM users WHERE id = :id";
        List<User> users = jdbc.query(sql, Map.of("id", id), mapper);
        return users.stream().findFirst();
    }

    @Override
    public User save(User user) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        String sql =  "INSERT INTO users (email, login, name, birthday)" +
                "VALUES (:email, :login, :name, :birthday)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("email", user.getEmail());
        params.addValue("login", user.getLogin());
        params.addValue("userName", user.getName());
        params.addValue("birthday", user.getBirthday());
        jdbc.update(sql, params, keyHolder, new String[]{"ID"});
        user.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return user;
    }

    @Override
    public User update(User user) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "UPDATE users " +
                "SET email = :email, login = :login, name = :name, birthday = :birthday " +
                "WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", user.getId());
        params.addValue("email", user.getEmail());
        params.addValue("login", user.getLogin());
        params.addValue("name", user.getName());
        params.addValue("birthday", user.getBirthday());

        int row = jdbc.update(sql, params, keyHolder);
        if (row != 1) throw new NotFoundException("User with id = " + user.getId() + " not found");
        return user;
    }

    @Override
    public void addFriend(long userId, long friendId) {
        String sqlStatuses = "SELECT STATUS FROM USER_FRIENDS WHERE USERID = :friendId AND FRIENDID = :userId";
        List<String> statuses = jdbc.queryForList(sqlStatuses, Map.of("userId", userId, "friendId", friendId), String.class);

        if (statuses.isEmpty()) {
            String insertSql = "INSERT INTO USER_FRIENDS (USER_ID, FRIEND_ID, STATUS) VALUES (:userId, :friendId, 'unconfirmed')";
            jdbc.update(insertSql, Map.of("userId", userId, "friendId", friendId));
        } else {
            String sqlUpdate = "UPDATE USER_FRIENDS SET STATUS = 'confirmed' WHERE (USER_ID = :userId AND FRIEND_ID = :friendId) OR (USERID = :friendId AND FRIENDID = :userId)";
            jdbc.update(sqlUpdate, Map.of("userId", userId, "friendId", friendId));

            String sqlInsert = "INSERT INTO USER_FRIENDS (USER_ID, FRIEND_ID, STATUS) VALUES (:userId, :friendId, 'confirmed')";
            jdbc.update(sqlInsert, Map.of("userId", friendId, "friendId", userId));
        }
    }

    @Override
    public void deleteFriend(long userId, long friendId) {
        String sqlDelete = "DELETE FROM USER_FRIENDS WHERE USER_ID = :userId AND FRIEND_ID = :friendId";
        jdbc.update(sqlDelete, Map.of("userId", userId, "friendId", friendId));
    }

    @Override
    public List<User> getFriends(long userId) {
        String sql = "SELECT u.* FROM USERS u INNER JOIN USER_FRIENDS f ON u.ID = f.FRIEND_ID " +
                "WHERE f.USER_ID = :userId";
        return jdbc.query(sql, Map.of("userId", userId), mapper);
    }

    @Override
    public List<User> getCommonFriends(long userId, long otherUserId) {
        checkExistUser(userId);
        checkExistUser(otherUserId);
        String sql = "SELECT u.* FROM USERS u " +
                "INNER JOIN USER_FRIENDS f1 ON u.ID = f1.FRIEND_ID " +
                "INNER JOIN USER_FRIENDS f2 ON u.ID = f2.FRIEND_ID " +
                "WHERE f1.USER_ID = :userId AND f2.USER_ID = :otherUserId";
        return jdbc.query(sql, Map.of("userId", userId, "otherUserId", otherUserId), new UserRowMapper());
    }

    @Override
    public void checkExistUser(Long id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", id);
        Boolean exist = jdbc.query(
                "SELECT * FROM USERS WHERE USER_ID = :userId",
                params, ResultSet::next);

        if (Boolean.FALSE.equals(exist)) {
            throw new NotFoundException("Нет пользователя с id = " + id);
        }
    }
}
