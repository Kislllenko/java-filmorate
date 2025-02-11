package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> storage = new HashMap<>();

    @Override
    public Film createFilm(Film film) {
        film.setId(getNextId());
        storage.put(film.getId(), film);
        log.info("Добавили {}", film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        storage.put(film.getId(), film);
        log.info("Обновили {}", film);
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        log.info("Получили список фильмов");
        return new ArrayList<>(storage.values());
    }

    @Override
    public List<Long> getAllFilmLikes(Long filmId) {
        Film film = get(filmId);
        return new ArrayList<>(film.getLikes());
    }

    @Override
    public boolean addLike(Long filmId, Long userId) {
        Film film = get(filmId);
        return film.addNewLike(userId);
    }

    @Override
    public boolean deleteLike(Long filmId, Long userId) {
        Film film = get(filmId);
        return film.deleteLike(userId);
    }

    @Override
    public List<Film> getPopularFilms(int count) {

        return getAllFilms()
                .stream()
                .sorted(Comparator.comparingInt(Film::getLikesCount).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public Film get(Long id) {
        return storage.get(id);
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
