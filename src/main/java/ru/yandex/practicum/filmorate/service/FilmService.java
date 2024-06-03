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

    public void addLike(long id, long userId) {
        userStorage.checkExistUser(userId);
        filmStorage.checkExistFilm(id);
        filmStorage.addLike(id, userId);
    }

    public void deleteLike(long filmId, long userId) {
        userStorage.checkExistUser(userId);
        filmStorage.checkExistFilm(filmId);
        filmStorage.deleteLike(filmId, userId);
    }

    public List<Film> getPopular(int count) {
        return filmStorage.getPopular(count);
    }
}
