package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.User;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")

public class UserController {

    private final HashMap<Integer, User> users = new HashMap<>();
    private Integer nextId = 1;


    @GetMapping()
    public List<User> listUsers() {

        List<User> listUsers = new ArrayList<>(users.values());

        log.info("Количество пользователей в списке: {}", listUsers.size());

        return listUsers;
    }

    @PostMapping()
    public User addUser(@RequestBody User user) {

        validateInfo(user);

        user.setId(nextId);
        nextId++;
        updateInfo(user);

        log.info("Сохранен пользователь: {}", user);
        return user;
    }

    @PutMapping()
    public User updateUser(@RequestBody User user) {

        if (!users.containsKey(user.getId())) {
            throw new UserDoesNotExistException("Такого пользователя нет в списке.");
        }

        validateInfo(user); // проверка данных на соответствие требуемому формату

        updateInfo(user);
        log.info("Обновлены данные пользователя {}", user); // сохранение информации о пользователе

        return user;
    }


    private void updateInfo(User user) {
        users.put(user.getId(), user);
    }

    private void validateInfo(User user) {

        if (isEmailInvalid(user)) {
            log.error("Не прошло проверку поле e-mail пользователя: {},", user.getEmail());
            throw new InvalidEmailException("Поле E-mail не должно быть пустым и должно содержать символ @.");
        }
        if (isLoginInvalid(user)) {
            log.error("Не прошло проверку поле Логин пользователя: {},", user.getLogin());
            throw new InvalidLoginException("Поле Логин пользователя не должно быть пустым и содержать пробелы.");
        }
        if (isUserNameEmpty(user)) {
            user.setName(user.getLogin());
        }
        if (isBirthdayInFuture(user)) {
            log.error("Не прошло проверку поле Дата рождения пользователя: {},", user.getBirthday());
            throw new BirthdayInFutureException("Дата рождения не может быть позже текущей даты.");
        }
        log.info("Введенные данные пользователя прошли проверку.");
    }

    private boolean isEmailInvalid(User user) {
        String email = user.getEmail();
        return isFieldEmpty(email) || !email.contains("@");
    }

    private boolean isLoginInvalid(User user) {
        String login = user.getLogin();
        return isFieldEmpty(login) || login.contains(" ");
    }

    private boolean isUserNameEmpty(User user) {
        String name = user.getName();
        return isFieldEmpty(name);

    }

    private boolean isBirthdayInFuture(User user) {
        return user.getBirthday().isAfter(LocalDate.now());
    }

    private boolean isFieldEmpty(String fieldValue) {
        return fieldValue == null || fieldValue.isBlank() || fieldValue.isEmpty();
    }


}






