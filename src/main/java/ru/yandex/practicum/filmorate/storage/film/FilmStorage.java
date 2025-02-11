package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import java.util.List;

public interface FilmStorage {

    Film createFilm(Film film);

    Film updateFilm(Film film);

    List<Film> getAllFilms();

    Film get(Long id);

    List<Long> getAllFilmLikes(Long userId);

    boolean addLike(Long filmId, Long userId);

    boolean deleteLike(Long filmId, Long userId);

    List<Film> getPopularFilms(int count);
}
