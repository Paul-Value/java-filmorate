package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.genre.GenreRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreRepository genreRepository;

    public Genre get(Long id) {
        return genreRepository.get(id)
                .orElseThrow(() -> new NotFoundException("Genre with the ID was not found " + id))
                ;
    }

    public List<Genre> getAll() {
        return genreRepository.getAll();
    }
}
