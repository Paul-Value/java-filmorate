package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.constraint.CorrectReleaseDate;

import java.time.LocalDate;
import java.util.LinkedHashSet;

/**
 * Film.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Film {
    public static final String RELEASE_DATE_LIMIT = "1895-12-28";
    private long id;

    @NotBlank
    private String name;

    @NotBlank
    @Size(max = 200)
    private String description;
    @CorrectReleaseDate(value = RELEASE_DATE_LIMIT)
    private LocalDate releaseDate;
    private LinkedHashSet<Genre> genres;
    private Mpa mpa;

    @Positive
    private int duration;
}
