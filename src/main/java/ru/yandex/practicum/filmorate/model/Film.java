package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.sql.Update;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

/**
 * Film.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Film {
    @NotNull
    private long id;

    @NotBlank
    private String name;

    @NotBlank
    @Size(max = 200)
    private String description;
    private LocalDate releaseDate;
    private List<Genre> genres;
    private MPA mpa;

    @Positive
    private int duration;

    @AssertTrue
    private boolean isValidReleaseDate() {
        return releaseDate.isAfter(LocalDate.of(1895, Month.DECEMBER, 28));
    }


}
