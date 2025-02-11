package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {

    UserService userService;

    @BeforeEach
    void setUp() {

        userService = new UserService(new InMemoryUserStorage());
    }

    @Test
    void shouldUseLoginIfUserNameNull() {

        User user = User.builder()
                .email("name@mail.ru")
                .name(null)
                .login("Login")
                .birthday(LocalDate.of(1988, 3, 12))
                .build();

        userService.validate(user);

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

        userService.validate(user);

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

        assertThrows(ValidationException.class, () -> userService.validate(user));
    }

    @Test
    public void shouldValidateLoginNotBlank() {

        User user = User.builder()
                .email("name@mail.ru")
                .login("")
                .name("User")
                .birthday(LocalDate.now())
                .build();

        assertThrows(ValidationException.class, () -> userService.validate(user));
    }

    @Test
    public void shouldValidateBirthdayInFuture() {

        User user = User.builder()
                .email("name@mail.ru")
                .login("Login")
                .name("User")
                .birthday(LocalDate.now().plusDays(1))
                .build();

        assertThrows(ValidationException.class, () -> userService.validate(user));
    }

    @Test
    public void shouldCreateCorrectUser() {

        User user = User.builder()
                .email("name@mail.ru")
                .login("Login")
                .name("User")
                .birthday(LocalDate.now())
                .build();

        userService.createUser(user);

        assertTrue(userService.getAllUsers().contains(user));

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

        List<User> listUsers = new ArrayList<>();

        listUsers.add(user1);
        listUsers.add(user2);

        userService.createUser(user1);
        userService.createUser(user2);

        assertEquals(userService.getAllUsers(), listUsers);
    }

    @Test
    public void shouldUpdateCreatedUser() {

        User user = User.builder()
                .email("name1@mail.ru")
                .login("Login")
                .name("User")
                .birthday(LocalDate.now())
                .build();

        userService.createUser(user);
        int allUsers = userService.getAllUsers().size();
        user.setLogin("updatedLogin");
        user.setName("updatedName");
        userService.updateUser(user);

        assertEquals(allUsers, userService.getAllUsers().size());
    }

    @Test
    public void shouldAddFriend() {

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

        userService.createUser(user1);
        userService.createUser(user2);

        userService.addFriend(user1.getId(),user2.getId());

        System.out.println(user1.getFriends());
        System.out.println(user2.getFriends());
    }
}
