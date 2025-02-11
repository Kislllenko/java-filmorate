package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class FilmService {

    private static final LocalDate LAST_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private final InMemoryFilmStorage inMemoryFilmStorage;
    private final InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage inMemoryFilmStorage, InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public Film createFilm(Film film) {
        validate(film);
        log.info("Добавление фильма : {}", film);
        return inMemoryFilmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        validate(film);
        log.info("Обновление данных о фильме : {}", film);
        if (inMemoryFilmStorage.get(film.getId()) == null) {
            log.warn("Обновление не выполнено, ID отсутствует в хранилище");
            throw new ValidationException("Обновление не выполнено, ID отсутствует в хранилище");
        }
        return inMemoryFilmStorage.updateFilm(film);
    }

    public List<Film> getAllFilms() {
        log.info("Текущее количество фильмов: {}", inMemoryFilmStorage.getAllFilms().size());
        return inMemoryFilmStorage.getAllFilms();
    }

    public boolean addLike(long filmId, long userId) {
        validateParameters(filmId, userId);
        log.info("Добавляем лайк от пользователя с ID: {} для фильма {}", userId, filmId);
        return inMemoryFilmStorage.addLike(filmId, userId);
    }

    public boolean deleteLike(long filmId, long userId) {
        validateParameters(filmId, userId);
        log.info("Удаляем лайк от пользователя с ID: {}", userId);
        return inMemoryFilmStorage.deleteLike(filmId, userId);
    }

    public List<Long> getAllFilmLikes(Long filmId) {
        validateParameter(filmId);
        log.info("Получаем все лайки фильма от пользователей");
        return inMemoryFilmStorage.getAllFilmLikes(filmId);
    }

    public List<Film> getPopularFilms(int count) {
        log.info("Получаем фильмы c наибольшим количеством лайков: {}", count);
        return inMemoryFilmStorage.getPopularFilms(count);
    }

    public Film getData(Long id) {
        if (inMemoryFilmStorage.get(id) == null) {
            log.info("Данные пользователя не найдены");
            throw new DataNotFoundException("Данные пользователя не найдены");
        }
        return inMemoryFilmStorage.get(id);
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

    public void validateParameter(Long filmId) {
        if (filmId == null) {
            throw new IncorrectParameterException("Некорректные параметры, необходимо проверить значение на null");
        }
        if (getData(filmId) == null) {
            throw new DataNotFoundException(String.format("Фильма с %d отсутствует", filmId));
        }
    }

    public void validateParameters(Long filmId, Long userId) {
        User user = inMemoryUserStorage.get(userId);
        Film film = getData(filmId);
        if (film == null || user == null) {
            log.info("Ошибка валидации. Необходимо проверить значение на null");
            log.info("Ошибка валидации. Такого ID пользователя: {} или фильма {} нет", userId, filmId);
            throw new IncorrectParameterException("Некорректные параметры, необходимо проверить значение на null");
        }
    }
}
