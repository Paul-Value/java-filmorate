package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.film.FilmRepository;
import ru.yandex.practicum.filmorate.repository.user.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {
    private final FilmRepository filmRepository;
    private final UserRepository userRepository;

    public Film save(Film film) {
        log.info("==>POST /films {}", film);
        Film newFilm = filmRepository.save(film);
        log.info("POST /films <== {}", newFilm);
        return newFilm;
    }

    public List<Film> getAll() {
        log.info("GET /films");
        return filmRepository.getAll();
    }

    public Film update(Film film) {
        filmRepository.checkExistFilm(film.getId());
        log.info("==>PUT /films {}", film);
        Film updatedFilm = filmRepository.update(film);
        log.info("PUT /users <== {}", updatedFilm);
        return filmRepository.update(film);
    }

    public void addLike(long id, long userId) {
        log.info("PUT /films/{}/like/{} add like", id, userId);
        userRepository.checkExistUser(userId);
        filmRepository.checkExistFilm(id);
        filmRepository.addLike(id, userId);
        log.info("PUT /films/{}/like/{} like added", id, userId);
    }

    public void deleteLike(long filmId, long userId) {
        log.info("DELETE /films/{}/like/{} delete", filmId, userId);
        userRepository.checkExistUser(userId);
        filmRepository.checkExistFilm(filmId);
        filmRepository.deleteLike(filmId, userId);
        log.info("DELETE /films/{}/like/{} deleted", filmId, userId);
    }

    public List<Film> getPopular(int count) {
        log.info("GET /films/popular?count={} get list of popular films", count);
        List<Film> films = filmRepository.getPopular(count);
        log.info("GET /films/popular?count={} list of popular films: {}", count, films.size());
        return films;
    }
}
