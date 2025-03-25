package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.genre.GenreRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreRepository genreRepository;

    public Genre get(Long id) {
        log.info("GET /genres");
        return genreRepository.get(id)
                .orElseThrow(() -> new NotFoundException("Genre with the ID was not found " + id));
    }

    public List<Genre> getAll() {
        log.info("GET /{genreId}");
        return genreRepository.getAll();
    }
}
