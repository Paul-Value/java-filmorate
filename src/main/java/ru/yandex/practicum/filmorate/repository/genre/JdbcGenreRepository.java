package ru.yandex.practicum.filmorate.repository.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcGenreRepository implements GenreRepository {
    private final NamedParameterJdbcOperations jdbc;
    private final GenreRowMapper mapper;

    @Override
    public List<Genre> getByIds(List<Long> ids) {
        String query = "SELECT * FROM GENRES WHERE id IN (:ids)";
        return jdbc.query(query, new MapSqlParameterSource("ids", ids), mapper);
    }

    @Override
    public Optional<Genre> get(Long id) {
        String sql = "SELECT * FROM genres WHERE id = :id";
        List<Genre> genres = jdbc.query(sql, Map.of("id", id), new GenreRowMapper());
        return genres.stream().findFirst();
    }

    @Override
    public List<Genre> getAll() {
        String query = "SELECT * FROM GENRES";
        return jdbc.query(query, mapper);
    }
}
