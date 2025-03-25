package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.mpa.MpaRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MpaService {
    private final MpaRepository mpaRepository;

    public Mpa get(Long id) {
        log.info("GET /mpa");
        return mpaRepository.get(id)
                .orElseThrow(() -> new NotFoundException("MPA with ID not found " + id));
    }

    public List<Mpa> getAll() {
        log.info("GET mpa/id");
        return mpaRepository.getAll();
    }
}
