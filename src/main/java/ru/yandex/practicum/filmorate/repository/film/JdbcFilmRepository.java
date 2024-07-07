package ru.yandex.practicum.filmorate.repository.film;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        String sql = "SELECT f.*, m.id as mpa_id, m.name as mpaRatingName, g.id as genre_id, g.name as genre_name FROM films f " +
                "LEFT JOIN MPA_RATING m ON f.mpaRatingId = m.id " +
                "LEFT JOIN FILM_GENRE fg ON f.id = fg.FILM_ID " +
                "LEFT JOIN GENRES g ON fg.GENRE_ID = g.id " +
                "WHERE f.id = :id";
        List<Film> films = jdbc.query(sql, Map.of("id", id), new FilmRowMapper());
        return films.stream().findFirst();
    }

    @Override
    public Film save(Film film) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO films (NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_RATING_ID) " +
                "VALUES (:name, :description, :releaseDate, :duration, :mpaRatingId)";

        HashMap<String, Object> params = new HashMap<>();
        params.put("name", film.getName());
        params.put("description", film.getDescription());
        params.put("releaseDate", film.getReleaseDate());
        params.put("duration", film.getDuration());
        params.put("mpaRatingId", film.getMpa().getId());
        MapSqlParameterSource parameterSource = new MapSqlParameterSource(params);

        jdbc.update(sql, parameterSource, keyHolder, new String[]{"ID"});
        Long filmId = keyHolder.getKey().longValue();
        film.setId(filmId);

        if (film.getGenre() != null) {
            for (Genre genre : film.getGenre()) {
                jdbc.update("INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) VALUES (:film_id, :genre_id)",
                        Map.of("film_id", filmId,
                                "genre_id", genre.getId()));
            }
        }

        MPA mpa = jdbc.queryForObject("SELECT * FROM MPA_RATING WHERE ID = :mpaRatingId",
                Map.of("mpaRatingId", film.getMpa().getId()), new BeanPropertyRowMapper<>(MPA.class));
        film.setMpa(mpa);

        List<Genre> genres = jdbc.query("SELECT g.* FROM GENRES g JOIN FILM_GENRE fg ON g.ID = fg.GENREID WHERE fg.FILMID = :filmId",
                Map.of("filmId", filmId), new BeanPropertyRowMapper<>(Genre.class));
        film.setGenre(genres);

        return film;
    }

    @Override
    public Film update(Film film) {
        String sql = "UPDATE films " +
                "SET name = :name, description = :description, releaseDate = :releaseDate, " +
                "duration = :duration, mpaRatingId = :mpaRatingId " +
                "WHERE id = :id";
        HashMap<String, Object> params = new HashMap<>();
        params.put("id", film.getId());
        params.put("name", film.getName());
        params.put("description", film.getDescription());
        params.put("releaseDate", film.getReleaseDate());
        params.put("duration", film.getDuration());
        params.put("mpaRatingId", film.getMpa().getId());

        jdbc.update(sql, params);

        if (film.getGenre() != null) {
            for (Genre genre : film.getGenre()) {
                jdbc.update("DELETE FROM FILM_GENRE WHERE FILMID = :film_id", Map.of("film_id", film.getId()));
                jdbc.update("INSERT INTO FILM_GENRE (FILMID, GENREID) VALUES (:film_id, :genre_id)",
                        Map.of("film_id", film.getId(),
                                "genre_id", genre.getId()));
            }
        }

        MPA mpa = jdbc.queryForObject("SELECT * FROM MPA_RATING WHERE ID = :mpaRatingId",
                Map.of("mpaRatingId", film.getMpa().getId()), new BeanPropertyRowMapper<>(MPA.class));
        film.setMpa(mpa);

        List<Genre> genres = jdbc.query("SELECT g.* FROM GENRES g JOIN FILM_GENRE fg ON g.ID = fg.GENREID WHERE fg.FILMID = :filmId",
                Map.of("filmId", film.getId()), new BeanPropertyRowMapper<>(Genre.class));
        film.setGenre(genres);

        return film;
    }

    @Override
    public void addLike(long filmId, long userId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", userId);
        params.addValue("filmId", filmId);
        jdbc.update("MERGE INTO USER_FILM_LIKES KEY (USER_ID, FILM_ID) VALUES(:userId, :filmId)", params);
    }

    @Override
    public void deleteLike(long filmId, long userId) {
        jdbc.update("DELETE FROM user_film_likes WHERE userId = :userId AND filmId = :filmId",
                Map.of("userId", userId, "filmId", filmId));
    }

    @Override
    public List<Film> getPopular(Integer count) {
        String sql = "SELECT f.*, m.id as mpa_id, m.name as mpaName, g.id as genre_id, g.name as genre_name, " +
                "COUNT(ufl.filmId) as likes " +
                "FROM films f " +
                "LEFT JOIN MPA_RATING m ON f.mpaRatingId = m.id " +
                "LEFT JOIN FILM_GENRE fg ON f.id = fg.FILM_ID " +
                "LEFT JOIN GENRES g ON fg.GENRE_ID = g.id " +
                "LEFT JOIN user_film_likes ufl ON f.id = ufl.filmId " +
                "GROUP BY f.id, m.id, g.id " +
                "ORDER BY likes DESC " +
                "LIMIT :count";
        return jdbc.query(sql, Map.of("count", count), new FilmRowMapper());
    }

    @Override
    public void checkExistFilm(Long filmId) {

    }
}
