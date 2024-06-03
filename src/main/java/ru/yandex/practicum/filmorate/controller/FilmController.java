package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;


    @GetMapping
    public List<Film> getAll() {
        log.info("GET /films");
        return filmService.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film create(@Valid @RequestBody Film film) {
        log.info("==>POST /films {}", film);
        Film newFilm = filmService.save(film);
        log.info("POST /films <== {}", newFilm);
        return newFilm;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("PUT /films {}", film);
        Film updatedFilm = filmService.update(film);
        log.info("PUT /films <== {}", updatedFilm);
        return updatedFilm;
    }

    @PutMapping("/{id}/like/{userId}")
    public void like(@PathVariable long id, @PathVariable long userId) {
        log.info("PUT /films/{}/like/{} add like", id, userId);
        filmService.addLike(id, userId);
        log.info("PUT /films/{}/like/{} like added", id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable long id, @PathVariable long userId) {
        log.info("DELETE /films/{}/like/{} delete", id, userId);
        filmService.deleteLike(id, userId);
        log.info("DELETE /films/{}/like/{} deleted", id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopular(@RequestParam(defaultValue = "10") @Positive int count) {
        log.info("GET /films/popular?count={} get list of popular films", count);
        List<Film> films = filmService.getPopular(count);
        log.info("GET /films/popular?count={} list of popular films: {}", count, films.size());
        return films;
    }
}
