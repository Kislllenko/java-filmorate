package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDate;
import java.util.HashSet;

/**
 * User.
 */
@Data
@EqualsAndHashCode(of = {"id"})
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    Long id;
    String email;
    String login;
    String name;
    LocalDate birthday;
    private final HashSet<Long> friends = new HashSet<>();

    public boolean addFriend(Long userId) {
        return friends.add(userId);
    }

    public boolean deleteFriend(Long userId) {
        return friends.remove(userId);
    }

    public HashSet<Long> getFriends() {
        return new HashSet<>(friends);
    }
}
