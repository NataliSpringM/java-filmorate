package ru.yandex.practicum.filmorate.controllers;

import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Validated
@Slf4j
@RequestMapping("/users")
public class UserController {

    /* обработка запросов HTTP-клиентов на добавление, обновление, получение информации о пользователях по адресу
    http://localhost:8080/users */

    private final HashMap<Integer, User> users = new HashMap<>();
    private Integer nextId = 1;

    @PostMapping()
    public User addUser(@Valid @RequestBody User user) { //добавление информации о пользователе

        user.setId(nextId);
        nextId++;

        if (isFieldEmpty(user.getName())) { // устанавливаем логин в качестве имени в случае незаполненного поля
            user.setName(user.getLogin());
        }

        updateInfo(user); // сохранение информации о пользователе
        log.info("Сохранен пользователь: {}", user);
        return user;
    }

    @PutMapping()
    public User updateUser(@Valid @RequestBody User user) { //обновление информации о пользователе

        if (!users.containsKey(user.getId())) {
            throw new UserDoesNotExistException("Такого пользователя нет в списке.");
        }
        if (isFieldEmpty(user.getName())) {
            user.setName(user.getLogin()); // устанавливаем логин в качестве имени в случае незаполненного поля
        }

        updateInfo(user); // обновление информации о пользователе
        log.info("Обновлены данные пользователя {}", user);
        return user;
    }

    @GetMapping()
    public List<User> listUsers() { //получение списка пользователей

        List<User> listUsers = new ArrayList<>(users.values());

        log.info("Количество пользователей в списке: {}", listUsers.size());

        return listUsers;
    }

    public Map<Integer, User> getUsersData() { // получение данных о пользователях для тестирования
        return users;
    }

    private void updateInfo(User user) { // сохранение данных
        users.put(user.getId(), user);
    }

    private boolean isFieldEmpty(String fieldValue) { //проверка является ли поле пустым
        return fieldValue == null || fieldValue.isBlank() || fieldValue.isEmpty();
    }

}






