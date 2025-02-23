package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FilmControllerTest {

    FilmService filmService;

    @BeforeEach
    void setUp() {

        filmService = new FilmService(new InMemoryFilmStorage(), new InMemoryUserStorage());
    }

    @Test
    void shouldValidateInvalidReleaseDate() {

        Film film = Film.builder()
                .name("Harry Potter")
                .description("Movie about young wizard")
                .duration(152)
                .releaseDate(LocalDate.of(1895, 12, 27))
                .build();

        assertThrows(ValidationException.class, () -> filmService.validate(film));
    }

    @Test
    void shouldCreateCorrectFilm() {

        Film film = Film.builder()
                .name("Harry Potter")
                .description("Movie about young wizard")
                .duration(152)
                .releaseDate(LocalDate.of(2001, 11, 4))
                .build();

        assertDoesNotThrow(() -> filmService.validate(film));
    }

    @Test
    void shouldReturnAllCreatedFilms() {

        Film film1 = Film.builder()
                .name("Harry Potter")
                .description("Movie about young wizard")
                .duration(152)
                .releaseDate(LocalDate.of(2001, 11, 4))
                .build();

        Film film2 = Film.builder()
                .name("Harry Potter and the Prisoner of Azkaban")
                .description("Movie about young wizard")
                .duration(142)
                .releaseDate(LocalDate.of(2004, 9, 16))
                .build();

        List<Film> listFilms = new ArrayList<>();

        listFilms.add(film1);
        listFilms.add(film2);

        filmService.createFilm(film1);
        filmService.createFilm(film2);

        assertEquals(filmService.getAllFilms(), listFilms);
    }

    @Test
    void shouldUpdateCreatedFilm() {

        Film film = Film.builder()
                .name("Harry Potter")
                .description("Movie about young wizard")
                .duration(152)
                .releaseDate(LocalDate.of(2001, 11, 4))
                .build();

        filmService.createFilm(film);
        int allFilms = filmService.getAllFilms().size();
        film.setName("Harry Potter and the Prisoner of Azkaban");
        film.setDuration(142);
        film.setReleaseDate(LocalDate.of(2004, 9, 16));
        filmService.updateFilm(film);

        assertEquals(allFilms, filmService.getAllFilms().size());
    }

    @Test
    public void shouldValidateInvalidFilmName() {

        Film film = Film.builder()
                .description("Movie about young wizard")
                .releaseDate(LocalDate.of(2001, 11, 4))
                .duration(120)
                .build();

        assertThrows(ValidationException.class, () -> filmService.validate(film));
    }

    @Test
    public void shouldValidateInvalidFilmDescription() {

        Film film = Film.builder()
                .name("Harry Potter")
                .description("q".repeat(201)) // >200
                .releaseDate(LocalDate.of(2001, 11, 4))
                .duration(120)
                .build();

        assertThrows(ValidationException.class, () -> filmService.validate(film));
    }

    @Test
    public void shouldValidateInvalidFilmReleaseDate() {

        Film film = Film.builder()
                .name("Harry Potter")
                .description("Movie about young wizard")
                .duration(120)
                .build();

        assertThrows(NullPointerException.class, () -> filmService.validate(film));
    }

    @Test
    public void shouldValidateInvalidFilmDuration() {

        Film film = Film.builder()
                .name("Film")
                .description("Description")
                .releaseDate(LocalDate.of(2001, 11, 4))
                .duration(-1)
                .build();

        assertThrows(ValidationException.class, () -> filmService.validate(film));
    }
}

