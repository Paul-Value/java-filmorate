package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Film save(Film film) {
        log.info("==>POST /films {}", film);
        Film newFilm = filmStorage.save(film);
        log.info("POST /films <== {}", newFilm);
        return filmStorage.save(newFilm);
    }

    public List<Film> getAll() {
        log.info("GET /films");
        return filmStorage.getAll();
    }

    public Film update(Film film) {
        log.info("PUT /films {}", film);
        Film updatedFilm = filmStorage.update(film);
        log.info("PUT /films <== {}", updatedFilm);
        return filmStorage.update(updatedFilm);
    }

    public void addLike(long id, long userId) {
        log.info("PUT /films/{}/like/{} add like", id, userId);
        userStorage.checkExistUser(userId);
        filmStorage.checkExistFilm(id);
        filmStorage.addLike(id, userId);
        log.info("PUT /films/{}/like/{} like added", id, userId);
    }

    public void deleteLike(long filmId, long userId) {
        log.info("DELETE /films/{}/like/{} delete", filmId, userId);
        userStorage.checkExistUser(userId);
        filmStorage.checkExistFilm(filmId);
        filmStorage.deleteLike(filmId, userId);
        log.info("DELETE /films/{}/like/{} deleted", filmId, userId);
    }

    public List<Film> getPopular(int count) {
        log.info("GET /films/popular?count={} get list of popular films", count);
        List<Film> films = filmStorage.getPopular(count);
        log.info("GET /films/popular?count={} list of popular films: {}", count, films.size());
        return films;
    }
}
