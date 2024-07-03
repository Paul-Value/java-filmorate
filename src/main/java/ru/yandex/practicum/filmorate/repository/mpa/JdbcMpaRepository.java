package ru.yandex.practicum.filmorate.repository.mpa;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcMpaRepository implements MpaRepository {
    private final NamedParameterJdbcOperations jdbc;
    private final MpaRowMapper mapper;

    @Override
    public Optional<MPA> get(Long id) {
        try {
            String query = "SELECT * FROM mpa_rating WHERE id = :id";
            List<MPA> mpaList = jdbc.query(query, Map.of("id", id), mapper);
            return mpaList.stream().findFirst();
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }

    @Override
    public List<MPA> getAll() {
        return jdbc.query("SELECT * FROM mpa_rating", mapper);
    }
}
