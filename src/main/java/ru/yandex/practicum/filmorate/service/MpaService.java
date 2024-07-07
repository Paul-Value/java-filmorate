package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.repository.mpa.MpaRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MpaService {
    private final MpaRepository mpaRepository;

    public MPA get(Long id) {
        return mpaRepository.get(id)
                .orElseThrow(() -> new NotFoundException("MPA with ID not found " + id));
    }

    public List<MPA> getAll() {
        return mpaRepository.getAll();
    }
}
