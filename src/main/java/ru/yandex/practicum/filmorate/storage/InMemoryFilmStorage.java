package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage{
    private final HashMap<Long, Film> films = new HashMap<>();
    private final HashMap<Long, Set<Long>> filmsLikes = new HashMap<>();


    @Override
    public List<Film> getAll() {
        return films.values().stream().toList();
    }

    @Override
    public Optional<Film> get(long filmId) {
        return Optional.ofNullable(films.get(filmId));
    }

    @Override
    public Film save(Film film) {
        film.setId(getNextId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new NotFoundException("Фильма с таким id не существует");
        }
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public void addLike(long filmId, long userId) {
        if (filmsLikes.containsKey(filmId)) {
            filmsLikes.get(filmId).add(userId);
        } else {
            Set<Long> likes = new HashSet<>();
            likes.add(userId);
            filmsLikes.put(filmId, likes);
        }
    }

    @Override
    public void deleteLike(long filmId, long userId) {
        if (!filmsLikes.containsKey(filmId)) {
            throw new NotFoundException(String.format("Фильм с id %d не найден", filmId));
        }
        if (!filmsLikes.get(filmId).contains(userId)) {
            throw new NotFoundException("Нет лайка от пользователя с id = " + userId);
        }
        filmsLikes.get(filmId).remove(userId);

    }

    @Override
    public List<Film> getPopular(Integer count) {
        return filmsLikes.entrySet().stream()
                .sorted(((o1, o2) -> o2.getValue().size() - o1.getValue().size()))
                .map(l -> films.get(l.getKey()))
                .toList();
    }

    @Override
    public void checkExistFilm(Long filmId) {
        if (!films.containsKey(filmId)) {
            throw new NotFoundException(String.format("Фильм с id %d не найден.", filmId));
        }
    }

    private Long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
