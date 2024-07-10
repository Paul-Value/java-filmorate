package ru.yandex.practicum.filmorate.repository.film;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@Import({JdbcFilmRepository.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class JdbcFilmRepositoryTest {
    private final JdbcFilmRepository filmRepository;

    static Film getTestFilm(int filmId) {
        List<Genre> genres1 = new ArrayList<>(List.of(new Genre(1L, "Комедия"), new Genre(2L, "Драма")));
        Film film1 = new Film(1L, "TJjtJuGS8dUeAzm", "HZOE3ct3plkt3m4ip6dN4EMzqop93SdO5QdJD16uzdhDIgUaQl",
                LocalDate.of(1962, 1, 31), genres1, new MPA(3, "PG-13"), 156);
        List<Genre> genres2 = new ArrayList<>(List.of(new Genre(3L, "Мультфильм")));
        Film film2 = new Film(2L, "8B8qgTBRtGKBdJN", "wrVuIIL79f228O2tecGsMdMVbltg1xKpz5qLz86LVHIOv9xJq1",
                LocalDate.of(1962, 3, 6), genres2, new MPA(4, "R"), 65);

        List<Genre> genres3 = new ArrayList<>(List.of(new Genre(4L, "Триллер")));
        Film film3 = new Film(3L, "uUk6L30WM5jNBHc", "cB22DWO2euD7py3KEnxpmqcBOh2sJZAOkHJP1pxgPlvbnuxEVW",
                LocalDate.of(1997, 03, 04), genres3, new MPA(1, "G"), 118);
        List<Film> films = new ArrayList<>(List.of(film1, film2, film3));
        return films.get(filmId - 1);
    }

    static Film getFilmForUpdate() {
        List<Genre> genres1 = new ArrayList<>(List.of(new Genre(2L, "Драма")));
        Film film = new Film(1L, "TJjtJuGS8dUeAzm", "HZOE3ct3plkt3m4ip6dN4EMzqop93SdO5QdJD16uzdhDIgUaQl",
                LocalDate.of(1962, 1, 31), genres1, new MPA(3, "PG-13"), 156);
        return film;
    }

    @Test
    void testGetById() {
        Film filmInData = filmRepository.get(1).get();
        assertThat(filmInData)
                .usingRecursiveComparison()
                .isEqualTo(getTestFilm(1));
    }

    @Test
    void testSave() {
        Film film = getFilmForUpdate();
        film.setId(1);
        Film updatedFilm = filmRepository.save(getFilmForUpdate());
        Film filmInData = filmRepository.get(updatedFilm.getId()).get();
        assertThat(filmInData)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(updatedFilm);
    }

    @Test
    void testUpdate() {
        Film filmInData = filmRepository.update(getFilmForUpdate());
        assertThat(filmInData)
                .usingRecursiveComparison()
                .isEqualTo(getFilmForUpdate());
    }

    @Test
    void testGetAll() {
        List<Film> filmsInData = filmRepository.getAll();
        assertEquals(3, filmsInData.size());
        assertThat(filmsInData)
                .contains(getTestFilm(1), getTestFilm(2), getTestFilm(3));
    }

}