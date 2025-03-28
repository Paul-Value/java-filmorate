package ru.yandex.practicum.filmorate.repository.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@Import({JdbcUserRepository.class, UserRowMapper.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class JdbcUserRepositoryTest {

    public static final long TEST_USER_ID = 1L;
    public static final long COMMON_FRIEND_USER_ID = 2L;
    private final JdbcUserRepository userRepository;

    @Test
    void testGetById() {
        User userInData = userRepository.get(TEST_USER_ID).get();
        assertThat(userInData)
                .usingRecursiveComparison()
                .isEqualTo(getTestUser(TEST_USER_ID));
    }

    static User getTestUser(long userId) {
        User user1 = new User(1L, "Katlynn17@yahoo.com", "karm0iglcY", "Mr. Kristi Senger", LocalDate.of(1977, 01, 07));
        User user2 = new User(2L, "Sierra28@hotmail.com", "bFiscFj1jl", "Eileen Mohr", LocalDate.of(1989, 07, 21));
        User user3 = new User(3L, "Buck.Rosenbaum@gmail.com", "HhWslNOalH", "Belinda Morissette", LocalDate.of(1966, 07, 26));
        LinkedList<User> users = new LinkedList<>(List.of(user1, user2, user3));
        return users.get((int) userId - 1);
    }

    static User getUserForUpdate() {
        User user = new User();
        user.setId(TEST_USER_ID);
        user.setName("testName");
        user.setEmail("test@gmail.com");
        user.setLogin("testLogin");
        user.setBirthday(LocalDate.of(1977, 1, 7));
        return user;
    }

    @Test
    void testGetAll() {
        List<User> usersInData = userRepository.getAll();
        assertThat(usersInData)
                .contains(getTestUser(1), getTestUser(2), getTestUser(3));
    }

    @Test
    void save() {
        User user = getUserForUpdate();
        user.setId(1);
        User updatedUser = userRepository.save(getUserForUpdate());
        User userInData = userRepository.get(updatedUser.getId()).get();
        assertThat(userInData)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(updatedUser);
    }

    @Test
    void testUpdate() {
        User userInData = userRepository.update(getUserForUpdate());
        assertThat(userInData)
                .usingRecursiveComparison()
                .isEqualTo(getUserForUpdate());
    }

    @Test
    void getCommonFriends() {
        List<User> usersInData = userRepository.getCommonFriends(getTestUser(1).getId(), getTestUser(3).getId());
        assertEquals(1, usersInData.size());
        assertThat(usersInData)
                .contains(getTestUser(COMMON_FRIEND_USER_ID));
    }

}