package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {

    UserController userController;

    @BeforeEach
    void setUp() {

        userController = new UserController();
    }

    @Test
    void shouldUseLoginIfUserNameNull() {

        User user = User.builder()
                .email("name@mail.ru")
                .name(null)
                .login("Login")
                .birthday(LocalDate.of(1988, 3, 12))
                .build();

        userController.validate(user);

        assertEquals("Login", user.getName());
    }

    @Test
    void shouldUseLoginIfUserNameBlank() {

        User user = User.builder()
                .email("name@mail.ru")
                .name("")
                .login("Login")
                .birthday(LocalDate.of(1999, 1, 22))
                .build();

        userController.validate(user);

        assertEquals("Login", user.getName());
    }

    @Test
    public void shouldValidateInvalidEmail() {

        User user = User.builder()
                .email("invalidEmail")
                .login("Login")
                .name("User")
                .birthday(LocalDate.now())
                .build();

        assertThrows(ValidationException.class, () -> userController.validate(user));
    }

    @Test
    public void shouldValidateLoginNotBlank() {

        User user = User.builder()
                .email("name@mail.ru")
                .login("")
                .name("User")
                .birthday(LocalDate.now())
                .build();

        assertThrows(ValidationException.class, () -> userController.validate(user));
    }

    @Test
    public void shouldValidateBirthdayInFuture() {

        User user = User.builder()
                .email("name@mail.ru")
                .login("Login")
                .name("User")
                .birthday(LocalDate.now().plusDays(1))
                .build();

        assertThrows(ValidationException.class, () -> userController.validate(user));
    }

    @Test
    public void shouldCreateCorrectUser() {

        User user = User.builder()
                .email("name@mail.ru")
                .login("Login")
                .name("User")
                .birthday(LocalDate.now())
                .build();

        userController.createUser(user);

        assertTrue(userController.getData().contains(user));

    }

    @Test
    public void shouldReturnAllCreatedUsers() {

        User user1 = User.builder()
                .email("name1@mail.ru")
                .login("Login1")
                .name("User1")
                .birthday(LocalDate.now())
                .build();

        User user2 = User.builder()
                .email("name2@mail.ru")
                .login("Login2")
                .name("User2")
                .birthday(LocalDate.now())
                .build();

        userController.createUser(user1);
        userController.createUser(user2);

        assertEquals(userController.getAllUsers(), userController.getData());
    }

    @Test
    public void shouldUpdateCreatedUser() {

        User user = User.builder()
                .email("name1@mail.ru")
                .login("Login")
                .name("User")
                .birthday(LocalDate.now())
                .build();

        userController.createUser(user);
        int allUsers = userController.getAllUsers().size();
        user.setLogin("updatedLogin");
        user.setName("updatedName");
        userController.updateUser(user);

        assertEquals(allUsers, userController.getAllUsers().size());
    }

}
