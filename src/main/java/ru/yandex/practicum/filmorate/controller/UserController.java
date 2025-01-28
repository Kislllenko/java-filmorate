package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final Map<Long, User> storage = new HashMap<>();

    @PostMapping
    public User createUser(@RequestBody User user) {

        log.info("Добавление пользователя: {}", user);
        validate(user);
        user.setId(getNextId());
        storage.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {

        log.info("Обновление данных пользователя : {}", user);
        validate(user);

        if (!storage.containsKey(user.getId())) {
            throw new ValidationException("Обновление не выполнено, id отсутствует в хранилище");
        }

        storage.put(user.getId(), user);
        return user;

    }

    @GetMapping
    public List<User> getAllUsers() {

        log.info("Текущее количество постов: {}", getData().size());
        return getData();
    }

    public List<User> getData() {

        return new ArrayList<>(storage.values());
    }

    public void validate(User user) {

        if (user.getName() == null || user.getName().isBlank()) {
            log.info("Имя для отображения пустое — использован логин : {}", user.getLogin());
            user.setName(user.getLogin());
        }
        if (!user.getEmail().contains("@")) {
            String msg = "Электронная почта не может быть пустой и должна содержать символ @";
            log.warn(msg);
            throw new ValidationException(msg);
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            String msg = "Дата рождения не может быть в будущем";
            log.warn(msg);
            throw new ValidationException(msg);
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            String msg = "Логин не может быть пустым и содержать пробелы";
            log.warn(msg);
            throw new ValidationException(msg);
        }
    }

    // вспомогательный метод для генерации идентификатора нового пользователя
    private long getNextId() {

        long currentMaxId = storage.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

}
