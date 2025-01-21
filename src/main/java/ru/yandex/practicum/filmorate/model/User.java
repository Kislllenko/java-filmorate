package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDate;

/**
 * User.
 */
@Data
@EqualsAndHashCode(of = {"id"})
@Builder
@FieldDefaults(level=AccessLevel.PRIVATE)
public class User {

    Long id;
    String email;
    String login;
    String name;
    LocalDate birthday;

}
