package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    UserController userController;
    User user;

    @BeforeEach
    void beforeEach() {
        userController = new UserController();
        user = new User(0, "petrov@yandex.ru", "Petrov99", "Petr",
                LocalDate.of(1999, Month.JUNE, 13));
    }

    @Test
    void create() {
        User newUser = userController.create(user);
        List<User> users = userController.getAll();
        assertEquals(newUser, users.getFirst());
    }

    @Test
    void update() {
        User newUser = userController.create(user);
        newUser.setLogin("SuperPetrov99");
        userController.update(newUser);
        List<User> users = userController.getAll();
        assertEquals(newUser, users.getFirst());
    }
}