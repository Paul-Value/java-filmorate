package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Film save(Film film) {
        return filmStorage.save(film);
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public void addLike(long userId, long filmId) {
        userStorage.checkExistUser(userId);
        filmStorage.checkExistFilm(filmId);
        filmStorage.addLike(filmId, userId);
    }

    public void deleteLike(long userId, long filmId) {
        userStorage.checkExistUser(userId);
        filmStorage.checkExistFilm(filmId);
        filmStorage.deleteLike(filmId, userId);
    }

    public List<Film> getPopular(Integer count) {
        if (count == null) {
            count = 10;
        }
        return filmStorage.getPopular(count);
    }
}
