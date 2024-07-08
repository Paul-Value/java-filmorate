package ru.yandex.practicum.filmorate.repository.film;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Component
public class FilmRowMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        MPA mpa = new MPA(rs.getLong("MPA_ID"), rs.getString("MPA.NAME"));

        film.setId(rs.getLong("FILM_ID"));
        film.setName(rs.getString("FILMS.NAME"));
        film.setDescription(rs.getString("DESCRIPTION"));
        film.setDuration(rs.getInt("DURATION"));
        film.setReleaseDate(rs.getDate("RELEASE_DATE").toLocalDate());
        film.setMpa(mpa);
        return film;
    }

/*    public Map<String, Object> filmToMap(Film film) {
        Map<String, Object> temp = new HashMap<>();
        temp.put("NAME", film.getName());
        temp.put("DESCRIPTION", film.getDescription());
        temp.put("RELEASE_DATE", film.getReleaseDate());
        temp.put("DURATION", film.getDuration());
        temp.put("MPA_RATING_ID", film.getMpa().getId());
        return temp;
    }*/
}
