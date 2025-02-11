package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class UserService {

    private final InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public UserService(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public User createUser(User user) {
        validate(user);
        log.info("Добавление пользователя: {}", user);
        return inMemoryUserStorage.createUser(user);
    }

    public User updateUser(User user) {
        validate(user);
        log.info("Обновление данных пользователя : {}", user);
        if (inMemoryUserStorage.get(user.getId()) == null) {
            log.warn("Обновление не выполнено, ID отсутствует в хранилище");
            throw new ValidationException("Обновление не выполнено, ID отсутствует в хранилище");
        }
        return inMemoryUserStorage.updateUser(user);
    }

    public List<User> getAllUsers() {
        log.info("Текущее количество постов: {}", inMemoryUserStorage.getAllUsers().size());
        return inMemoryUserStorage.getAllUsers();
    }

    public List<User> getAllFriends(Long userId) {
        validateParameter(userId);
        log.info("Получаем список друзей");
        return inMemoryUserStorage.getAllFriends(userId);
    }

    public boolean addFriend(Long userId, Long friendId) {
        validateParameters(userId, friendId);
        log.info("Добавляем пользователю ID: {}, друга с friendId: {}", userId, friendId);
        return inMemoryUserStorage.addFriend(userId, friendId);
    }

    public boolean deleteFriend(Long userId, Long friendId) {
        validateParameters(userId, friendId);
        log.info("Удаляем у пользователя ID: {} друга с friendId: {}", userId, friendId);
        return inMemoryUserStorage.deleteFriend(userId, friendId);
    }

    public List<User> getCommonFriends(Long userId, Long friendId) {
        validateParameters(userId, friendId);
        log.info("Получаем список общих друзей пользователей ID: {} и {}", userId, friendId);
        return inMemoryUserStorage.getCommonFriends(userId, friendId);
    }

    public User getData(Long id) {
        if (inMemoryUserStorage.get(id) == null) {
            log.info("Данные пользователя не найдены");
            throw new DataNotFoundException("Данные пользователя не найдены");
        }
        return inMemoryUserStorage.get(id);
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

    public void validateParameter(Long userId) {
        if (userId == null) {
            throw new IncorrectParameterException("Некорректные параметры, необходимо проверить значение на null");
        }
        if (getData(userId) == null) {
            throw new DataNotFoundException(String.format("Пользователь с %d отсутствует", userId));
        }
    }

    public void validateParameters(Long userId, Long friendId) {
        User user = getData(userId);
        User friend = getData(friendId);
        log.info("Валидация параметров UserService");
        if (user == null || friend == null) {
            throw new DataNotFoundException("Друг не добавлен, таких пользователей нет");
        }
    }
}
