package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @PostMapping
    public Film createFilm(@RequestBody Film film) {
        return filmService.createFilm(film);
    }

    @PutMapping()
    public Film updateFilm(@RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @GetMapping("/{id}")
    public Film getFilm(@RequestBody @PathVariable Long id) {
        log.info("Получаем объект по id: {}", id);
        return filmService.getData(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public boolean likeFilm(@RequestBody @PathVariable Long id, @PathVariable Long userId) {
        log.info("Пользователь с id: {} пытается поставить лайк фильму с айди {}", id, userId);
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public boolean removeFilmLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Пользователь с id: {} пытается удалить лайк у фильма с айди {}", id, userId);
        return filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        log.info("Пытаемся получить фильмы с наибольшим количеством лайков: {} шт.", count);
        return filmService.getPopularFilms(count);
    }
}
