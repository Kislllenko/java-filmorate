package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDate;
import java.util.HashSet;

/**
 * Film.
 */
@Data
@EqualsAndHashCode(of = {"id"})
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Film {

    Long id;
    String name;
    String description;
    LocalDate releaseDate;
    int duration;
    private final HashSet<Long> likes = new HashSet<>();

    public int getLikesCount() {
        return likes.size();
    }

    public boolean addNewLike(Long userId) {
        return likes.add(userId);
    }

    public boolean deleteLike(Long userId) {
        return likes.remove(userId);
    }

    public HashSet<Long> getLikes() {
        return new HashSet<>(likes);
    }
}
