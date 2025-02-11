package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;
import java.util.List;

public interface UserStorage {

    User createUser(User user);

    User updateUser(User user);

    List<User> getAllUsers();

    User get(Long id);

    List<User> getAllFriends(Long userId);

    boolean addFriend(Long userId, Long friendId);

    boolean deleteFriend(Long userId, Long friendId);
}
