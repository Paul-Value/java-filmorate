package ru.yandex.practicum.filmorate.repository.film;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.repository.genre.GenreRowMapper;
import ru.yandex.practicum.filmorate.repository.mpa.MpaRowMapper;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@Primary
@RequiredArgsConstructor
public class JdbcFilmRepository implements FilmRepository {
    private final NamedParameterJdbcOperations jdbc;
    private final FilmRowMapper mapper;

    @Override
    public List<Film> getAll() {
        String sql = "SELECT f.*, m.id as mpaId, m.name as mpaRatingName, g.id as genreId, g.name as genreName " +
                "FROM films f " +
                "LEFT JOIN MPA_RATING m ON f.mpa_rating_id = m.id " +
                "LEFT JOIN FILM_GENRE fg ON f.id = fg.film_id " +
                "LEFT JOIN GENRES g ON fg.genre_id = g.id";
        return jdbc.query(sql, mapper);
    }

    @Override
    public Optional<Film> get(long id) {
        String sql = "SELECT f.*, m.id as mpaId, m.name as mpaRatingName, g.id as genreId, g.name as genreName " +
                "FROM films f " +
                "LEFT JOIN MPA_RATING m ON f.mpa_rating_id = m.id " +
                "LEFT JOIN FILM_GENRE fg ON f.id = fg.film_id " +
                "LEFT JOIN GENRES g ON fg.genre_id = g.id " +
                "WHERE f.id = :id";
        List<Film> films = jdbc.query(sql, Map.of("id", id), mapper);
        return films.stream().findFirst();
    }

    //@SuppressWarnings("DataFlowIssue")
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
        film.setId(keyHolder.getKeyAs(Long.class));

        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                jdbc.update("INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) VALUES (:filmId, :genreId)",
                        Map.of("filmId", film.getId(),
                                "genreId", genre.getId()));
            }
        }

        MPA mpa = jdbc.queryForObject("SELECT * FROM MPA_RATING WHERE ID = :mpaRatingId",
                Map.of("mpaRatingId", film.getMpa().getId()), new MpaRowMapper());
        film.setMpa(mpa);

        List<Genre> genres = jdbc.query("SELECT g.* FROM GENRES g " +
                        "JOIN FILM_GENRE fg ON g.ID = fg.GENRE_ID" +
                        " WHERE fg.FILM_ID = :filmId",
                Map.of("filmId", film.getId()), new GenreRowMapper());
        film.setGenres(genres);

        return film;
    }

    @Override
    public Film update(Film film) {
        String sql = "UPDATE films " +
                "SET NAME = :name, DESCRIPTION = :description, RELEASE_DATE = :releaseDate, DURATION = :duration," +
                " MPA_RATING_ID = :mpaRatingId" +
                " WHERE id = :id";
        HashMap<String, Object> params = new HashMap<>();
        params.put("id", film.getId());
        params.put("name", film.getName());
        params.put("description", film.getDescription());
        params.put("releaseDate", film.getReleaseDate());
        params.put("duration", film.getDuration());
        params.put("mpaRatingId", film.getMpa().getId());

        jdbc.update(sql, params);

        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                jdbc.update("DELETE FROM FILM_GENRE " +
                        "WHERE FILM_ID = :film_id", Map.of("film_id", film.getId()));
                jdbc.update("INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) VALUES (:film_id, :genre_id)",
                        Map.of("film_id", film.getId(),
                                "genre_id", genre.getId()));
            }
        }

        MPA mpa = jdbc.queryForObject("SELECT * FROM MPA_RATING" +
                        " WHERE ID = :mpaRatingId",
                Map.of("mpaRatingId", film.getMpa().getId()), new MpaRowMapper());
        film.setMpa(mpa);

        List<Genre> genres = jdbc.query("SELECT g.* FROM GENRES g " +
                        "JOIN FILM_GENRE fg ON g.ID = fg.GENRE_ID WHERE fg.FILM_ID = :filmId",
                Map.of("filmId", film.getId()), new GenreRowMapper());
        film.setGenres(genres);

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
        jdbc.update("DELETE FROM USER_FILM_LIKES" +
                        " WHERE USER_Id = :userId AND FILM_ID = :filmId",
                Map.of("userId", userId, "filmId", filmId));
    }

    @Override
    public List<Film> getPopular(Integer count) {
        String sql = "SELECT f.*, m.id as mpaId, m.name as mpaName, g.id as genreId, g.name as genreName, " +
                "COUNT(ufl.film_id) as likes " +
                "FROM films f " +
                "LEFT JOIN MPA_RATING m ON f.mpa_rating_id = m.id " +
                "LEFT JOIN FILM_GENRE fg ON f.id = fg.film_id " +
                "LEFT JOIN GENRES g ON fg.genre_id = g.id " +
                "LEFT JOIN USER_FILM_LIKES ufl ON f.id = ufl.film_id " +
                "GROUP BY f.id, m.id, g.id " +
                "ORDER BY likes DESC " +
                "LIMIT :count";
        return jdbc.query(sql, Map.of("count", count), mapper);
    }

    @Override
    public void checkExistFilm(Long filmId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("filmId", filmId);
        Boolean exist = jdbc.query(
                "SELECT * FROM FILMS WHERE ID = :filmId",
                params, ResultSet::next);

        if (Boolean.FALSE.equals(exist)) {
            throw new NotFoundException("Film with id = " + filmId + " not found");
        }
    }
}
