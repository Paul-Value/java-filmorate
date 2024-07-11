package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IllegalBodyRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.film.FilmRepository;
import ru.yandex.practicum.filmorate.repository.genre.GenreRepository;
import ru.yandex.practicum.filmorate.repository.mpa.MpaRepository;
import ru.yandex.practicum.filmorate.repository.user.UserRepository;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {
    private final FilmRepository filmRepository;
    private final UserRepository userRepository;
    private final GenreRepository genreRepository;
    private final MpaRepository mpaRepository;

    public Film save(Film film) {
        log.info("==>POST /films {}", film);
        mpaValidate(film);
        if (film.getGenres() != null) {
            film.setGenres(getUniqueGenres(film));
            genresValidation(film);
        }
        Film newFilm = filmRepository.save(film);
        log.info("POST /films <== {}", newFilm);
        return newFilm;
    }

    public Film get(Long id) {
        return filmRepository.get(id)
                .orElseThrow(() -> new NotFoundException("Film with the ID was not found " + id));
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

    private void mpaValidate(Film film) {
        long filmMpaId = film.getMpa().getId();
        List<Long> mpaIds = mpaRepository.getAll()
                .stream()
                .map(Mpa::getId).toList();
        if (!mpaIds.contains(filmMpaId)) {
            throw new IllegalBodyRequestException("MPA with ID: " + filmMpaId + " not exist");
        }
    }

    private LinkedHashSet<Genre> getUniqueGenres(Film film) {
        List<Genre> genres = film.getGenres().stream()
                .collect(Collectors.toMap(Genre::getId, genre -> genre, (oldValue, newValue) -> oldValue))
                .values()
                .stream()
                .toList();
        return new LinkedHashSet<>(genres);
    }

    private void genresValidation(Film film) {
        List<Long> genreIds = genreRepository.getAll()
                .stream()
                .map(Genre::getId)
                .toList();

        for (Genre genre : film.getGenres()) {
            if (!genreIds.contains(genre.getId())) {
                throw new IllegalBodyRequestException("Genre with ID:" + genre.getId() + " not exist");
            }
        }
    }
}
