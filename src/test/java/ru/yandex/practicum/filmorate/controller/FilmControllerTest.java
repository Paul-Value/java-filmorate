package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Set;

import static jakarta.validation.Validation.buildDefaultValidatorFactory;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FilmControllerTest {
    FilmController filmController;
    Film film;


    ValidatorFactory factory = buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();

    @BeforeEach
    void beforeEach() {
        filmController = new FilmController();
        film = new Film(0, "The Green Mile", "Sad film", LocalDate.of(1999, Month.DECEMBER,
                6), 189);
    }

    @Test
    void create() {
        Film newFilm = filmController.create(film);
        List<Film> films = filmController.getAll();
        assertEquals(newFilm, films.getFirst());
    }

    @Test
    void update() {
        Film newFilm = filmController.create(film);
        newFilm.setDescription("Good but sad film");
        Film newFilm2 = filmController.update(newFilm);
        List<Film> films = filmController.getAll();
        assertEquals(newFilm2, films.getFirst());
    }

    @Test
    void shouldNotValidateBigDescription() {
        Film newFilm = filmController.create(new Film(0, " ", "Sad film dfdfdfddfdfdfdfdf" +
                "adadadadaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaabdfsfsfs" +
                "fsffsfssflllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllll",
                LocalDate.of(1850, Month.DECEMBER,
                        6), -189));
        Set<ConstraintViolation<Film>> violations = validator.validate(newFilm);
        assertEquals(4, violations.size());
    }

    @Test
    void shouldNotValidateBlankName() {
        Film newFilm = filmController.create(film);
        newFilm.setName(" ");
        Set<ConstraintViolation<Film>> violations = validator.validate(newFilm);
        assertEquals(1, violations.size());
    }
}