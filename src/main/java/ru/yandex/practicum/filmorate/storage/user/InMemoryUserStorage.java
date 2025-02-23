package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> storage = new HashMap<>();

    @Override
    public User createUser(User user) {
        user.setId(getNextId());
        storage.put(user.getId(), user);
        log.info("Добавили {}", user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        storage.put(user.getId(), user);
        log.info("Обновили {}", user);
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        log.info("Получили список пользователей");
        return new ArrayList<>(storage.values());
    }

    @Override
    public List<User> getAllFriends(Long userId) {
        User user = get(userId);
        ArrayList<Long> friensIds = new ArrayList<>(user.getFriends());
        log.info("Список друзей получен {} ", friensIds.size());
        log.info("Список друзей: {}", friensIds);
        ArrayList<User> allFriends = new ArrayList<>();
        for (Long friendId : friensIds) {
            allFriends.add(get(friendId));
        }
        return allFriends;
    }

    @Override
    public boolean addFriend(Long userId, Long friendId) {
        User user = get(userId);
        User friend = get(friendId);
        boolean isAdded = (user.addFriend(friendId) && friend.addFriend(userId));
        log.info("Друг добавлен");
        log.info("Друг: {}", get(friendId));
        return isAdded;
    }

    @Override
    public boolean deleteFriend(Long userId, Long friendId) {
        User user = get(userId);
        User friend = get(friendId);
        boolean isDeleted = (user.deleteFriend(friendId) && friend.deleteFriend(userId));
        log.info("Друг удален");
        return isDeleted;
    }

    public List<User> getCommonFriends(Long userId, Long friendId) {
        User user = get(userId);
        User friend = get(friendId);

        List<User> commonFriendsList = new ArrayList<>();
        for (Long commonFriendId : user.getFriends()) {
            if (friend.getFriends().contains(commonFriendId)) {
                commonFriendsList.add(get(commonFriendId));
            }
        }
        log.info("Получен список общих друзей: {}", commonFriendsList.size());
        log.info("Список общих друзей: {}", commonFriendsList);
        return commonFriendsList;
    }

    @Override
    public User get(Long id) {
        return storage.get(id);
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
