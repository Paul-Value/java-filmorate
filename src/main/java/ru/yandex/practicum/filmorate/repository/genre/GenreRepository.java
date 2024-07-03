package ru.yandex.practicum.filmorate.repository.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreRepository {
    List<Genre> getByIds(List<Long> ids);

    Optional<Genre> get(Long id);

    List<Genre> getAll();
}
