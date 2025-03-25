package ru.yandex.practicum.filmorate.repository.film;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.genre.GenreRowMapper;

import java.sql.ResultSet;
import java.util.*;

@Repository
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
        String sql = "SELECT FILMS.ID, FILMS.NAME, FILMS.DESCRIPTION, FILMS.RELEASE_DATE, " +
                "FILMS.DURATION, MPA_RATING.ID,MPA_RATING.NAME, GENRES.ID, GENRES.NAME   " +
                "FROM FILMS " +
                "join MPA_RATING on FILMS.MPA_RATING_ID = MPA_RATING.ID " +
                "left join FILM_GENRE on FILMS.ID = FILM_GENRE.FILM_ID " +
                "left join GENRES on FILM_GENRE.GENRE_ID = GENRES.ID WHERE FILMS.id = :filmId";
        return Optional.ofNullable(jdbc.query(sql, Map.of("filmId", id), new FIlmExtractor()));
    }

    @Override
    public Film save(Film film) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        Map<String, Object> map = Map.of("name", film.getName(),
                "description", film.getDescription(),
                "releaseDate", film.getReleaseDate(),
                "duration", film.getDuration(),
                "MPA_RATING_ID", film.getMpa().getId());
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValues(map);
        jdbc.update("INSERT INTO FILMS (name,description,release_Date,duration,MPA_RATING_ID)" +
                        " VALUES(:name,:description,:releaseDate,:duration,:MPA_RATING_ID)",
                params, keyHolder, new String[]{"ID"});

        film.setId(keyHolder.getKeyAs(Long.class));
        saveFilmGenres(film);
        return film;
    }

    @Override
    public Film update(Film film) {
        Map<String, Object> map = Map.of("filmId", film.getId(),
                "name", film.getName(),
                "description", film.getDescription(),
                "releaseDate", film.getReleaseDate(),
                "duration", film.getDuration(),
                "MPA_RATING_ID", film.getMpa().getId());
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValues(map);
        jdbc.update("UPDATE FILMS " +
                " SET NAME=:name,DESCRIPTION=:description, RELEASE_DATE=:releaseDate,DURATION=:duration,MPA_RATING_ID=:MPA_RATING_ID " +
                "WHERE FILMS.ID=:filmId", params);
        removeFilmGenres(film);
        saveFilmGenres(film);
        return get(film.getId()).get();
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
        final List<Genre> genres = getAllGenres();
        final List<Film> films = jdbc.query("SELECT FILMS.ID, FILMS.NAME, DESCRIPTION, RELEASE_DATE, DURATION, " +
                        "FILMS.MPA_RATING_ID, MPA_RATING.NAME as MPARATINGNAME " +
                        "FROM FILMS " +
                        "JOIN MPA_RATING on FILMS.MPA_RATING_ID = MPA_RATING.ID " +
                        "LEFT JOIN USER_FILM_LIKES on FILMS.ID = USER_FILM_LIKES.FILM_ID " +
                        "GROUP BY FILMS.ID " +
                        "ORDER BY COUNT(USER_FILM_LIKES.USER_ID) desc " +
                        "LIMIT :count",
                Map.of("count", count), mapper);
        final Map<Long, LinkedHashSet<Genre>> filmGenres = getAllFilmsGenres(genres);

        films.forEach(film -> film.setGenres(filmGenres.getOrDefault(film.getId(), new LinkedHashSet<>())));
        return films;
    }

    public List<Genre> getAllGenres() {
        return jdbc.query("SELECT * FROM GENRES", new GenreRowMapper());
    }

    Map<Long, LinkedHashSet<Genre>> getAllFilmsGenres(final List<Genre> allGenres) {
        final Map<Long, LinkedHashSet<Genre>> filmGenres = new HashMap<>();
        jdbc.query("SELECT * FROM FILM_GENRE", rs -> {
                    while (rs.next()) {
                        final long filmId = rs.getLong("film_id");
                        final long genreId = rs.getLong("genre_id");
                        final Genre genre = allGenres.stream()
                                .filter(g -> g.getId() == genreId)
                                .findFirst()
                                .get();
                        filmGenres.computeIfAbsent(filmId, k -> new LinkedHashSet<>()).add(genre);
                    }
                }
        );
        return filmGenres;
    }

    private void saveFilmGenres(Film film) {
        if (film.getGenres() == null || film.getGenres().isEmpty()) {
            film.setGenres(null);
            return;
        }
        var batchValue = film.getGenres().stream()
                .map(genre -> new MapSqlParameterSource()
                        .addValue("film_id", film.getId())
                        .addValue("genre_id", genre.getId()))
                .toList();

        jdbc.batchUpdate("INSERT INTO FILM_GENRE (film_id,genre_id) VALUES (:film_id,:genre_id)",
                batchValue.toArray(new SqlParameterSource[0]));

    }

    private void removeFilmGenres(Film film) {
        jdbc.update("DELETE FROM FILM_GENRE WHERE film_id = :filmId",
                Map.of("filmId", film.getId()));
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
