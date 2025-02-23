package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUser(@RequestBody @PathVariable Long id) {
        log.info("Получаем объект по id: {}", id);
        return userService.getData(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public boolean addFriends(@RequestBody @PathVariable Long id, @PathVariable Long friendId) {
        log.info("Добавляем пользователю ID: {}, друга с friendId: {}", id, friendId);
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public boolean removeFriend(@RequestBody @PathVariable Long id, @PathVariable Long friendId) {
        log.info("Удаляем у пользователя ID: {} друга с friendId: {}", id, friendId);
        return userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriendsList(@RequestBody @PathVariable Long id) {
        log.info("Получаем список друзей пользователя ID: {}", id);
        return userService.getAllFriends(id);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public List<User> getCommonFriend(@RequestBody @PathVariable Long id, @PathVariable Long otherId) {
        log.info("Получаем список общих друзей пользователей ID: {} и {}", id, otherId);
        return userService.getCommonFriends(id, otherId);
    }
}
