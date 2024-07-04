package ru.yandex.practicum.filmorate.repository.film;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcFilmRepository implements FilmRepository {
    private final NamedParameterJdbcOperations jdbc;

    @Override
    public List<Film> getAll() {
        String sql = "SELECT f.*, m.id as mpa_id, m.name as mpaRatingName, g.id as genre_id, g.name as genre_name FROM films f " +
                "LEFT JOIN MPA_RATING m ON f.mpaRatingId = m.id " +
                "LEFT JOIN FILM_GENRE fg ON f.id = fg.FILM_ID " +
                "LEFT JOIN GENRES g ON fg.GENRE_ID = g.id";
        return jdbc.query(sql, new FilmRowMapper());
    }

    @Override
    public Optional<Film> get(long id) {
        return Optional.empty();
    }

    @Override
    public Film save(Film film) {
        return null;
    }

    @Override
    public Film update(Film film) {
        return null;
    }

    @Override
    public void addLike(long filmId, long userId) {

    }

    @Override
    public void deleteLike(long filmId, long userId) {

    }

    @Override
    public List<Film> getPopular(Integer count) {
        return List.of();
    }

    @Override
    public void checkExistFilm(Long filmId) {

    }
}
