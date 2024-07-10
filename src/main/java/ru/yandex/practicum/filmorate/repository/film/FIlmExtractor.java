package ru.yandex.practicum.filmorate.repository.film;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FIlmExtractor implements ResultSetExtractor<Film> {
    @Override
    public Film extractData(ResultSet rs) throws SQLException, DataAccessException {
        if (!rs.isBeforeFirst()) {
            return null;
        }
        List<Genre> genresSet = new ArrayList<>();
        Film film = new Film();
        while (rs.next()) {
            film.setId(rs.getLong("FILMS.ID"));
            film.setName(rs.getString("FILMS.NAME"));
            film.setDescription(rs.getString("FILMS.DESCRIPTION"));
            film.setDuration(rs.getInt("FILMS.DURATION"));
            film.setReleaseDate(rs.getDate("FILMS.RELEASE_DATE").toLocalDate());
            film.setMpa(new MPA(rs.getInt("MPA_RATING.ID"), rs.getString("MPA_RATING.NAME")));
            long genreId = rs.getLong("GENRES.ID");
            if (!rs.wasNull()) {
                genresSet.add(new Genre(genreId, rs.getString("GENRES.NAME")));
            }
        }

        film.setGenres(genresSet);
        return film;
    }
}
