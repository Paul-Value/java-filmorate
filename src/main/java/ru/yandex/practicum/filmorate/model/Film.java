package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

/**
 * Film.
 */
@Data
@AllArgsConstructor
public class Film {
    private long id;

    @NotBlank
    private String name;

    @NotBlank
    @Size(max = 200)
    private String description;
    private LocalDate releaseDate;
    private List<String> genre;
    private MPA mpa;

    @Positive
    private int duration;

    @AssertTrue
    private boolean isValidReleaseDate() {
        return releaseDate.isAfter(LocalDate.of(1895, Month.DECEMBER, 28));
    }


}
