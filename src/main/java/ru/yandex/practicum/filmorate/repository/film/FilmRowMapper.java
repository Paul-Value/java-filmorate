package ru.yandex.practicum.filmorate.repository.film;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class FilmRowMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        MPA mpa = new MPA();
        mpa.setId(rs.getLong("MPA_RATING_ID"));
        mpa.setName(rs.getString("MPA_RATING_NAME"));

        film.setId(rs.getInt("ID"));
        film.setName(rs.getString("NAME"));
        film.setDescription(rs.getString("DESCRIPTION"));
        film.setReleaseDate(LocalDate.parse(rs.getString("RELEASE_DATE")));
        film.setDuration(rs.getInt("DURATION"));
        film.setMpa(mpa);

        long genreId = rs.getLong("GENRE_ID");
        if(genreId > 0) {
            Genre genre = new Genre();
            genre.setId(genreId);
            genre.setName(rs.getString("GENRE_NAME"));
            film.getGenre().add(genre);
        }
        return film;
    }

    public Map<String, Object> filmToMap(Film film) {
        Map<String, Object> temp = new HashMap<>();
        temp.put("NAME", film.getName());
        temp.put("DESCRIPTION", film.getDescription());
        temp.put("RELEASE_DATE", film.getReleaseDate());
        temp.put("DURATION", film.getDuration());
        temp.put("MPA_RATING_ID", film.getMpa().getId());
        return temp;
    }
}
