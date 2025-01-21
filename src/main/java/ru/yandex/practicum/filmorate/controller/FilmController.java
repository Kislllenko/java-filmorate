package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final Map<Long, Film> storage = new HashMap<>();
    private static final LocalDate LAST_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    @PostMapping
    public Film createFilm(@RequestBody Film film) {

        log.info("Добавление фильма : {}", film);
        validate(film);
        film.setId(getNextId());
        storage.put(film.getId(), film);
        return film;
    }

    @PutMapping() //"/{id}"
    public Film updateFilm(@RequestBody Film film) {

        log.info("Пытаемся обновить фильм : {}", film);
        validate(film);

        if (!storage.containsKey(film.getId())) {
            throw new ValidationException("Обновление не выполнено, id отсутствует в хранилище");
        }

        storage.put(film.getId(), film);
        return film;
    }

    @GetMapping
    public List<Film> getAllFilms() {

        log.info("Текущее количество фильмов: {}", getData().size());
        return getData();
    }

    public List<Film> getData() {

        return new ArrayList<>(storage.values());
    }

    public void validate(Film film) {

        if (film.getReleaseDate().isBefore(LAST_RELEASE_DATE)) {
            log.warn("Дата выпуска меньше 1895.12.28 : {}", film.getReleaseDate());
            throw new ValidationException("Дата выпуска меньше 1895.12.28");
        }
        if (film.getName() == null || film.getName().isBlank()) {
            String msg = "Название не может быть null или пустым";
            log.warn(msg);
            throw new ValidationException(msg);
        }
        if (film.getDescription().length() > 200) {
            String msg = "Максимальная длина описания — 200 символов";
            log.warn(msg);
            throw new ValidationException(msg);
        }
        if (film.getDuration() <= 0) {
            String msg = "Продолжительность фильма не может быть <= 0";
            log.warn(msg);
            throw new ValidationException(msg);
        }
    }

    // вспомогательный метод для генерации идентификатора нового фильма
    private long getNextId() {

        long currentMaxId = storage.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
