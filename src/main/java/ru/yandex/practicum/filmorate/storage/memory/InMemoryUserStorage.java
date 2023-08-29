package ru.yandex.practicum.filmorate.storage.memory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Slf4j
@Component

public class InMemoryUserStorage implements UserStorage {

    // реализация хранения информации о пользователях в памяти

    private final HashMap<Long, User> users = new HashMap<>();

    private Long nextId = 1L;

    //добавление информации о пользователе
    @Override
    public User addUser(User user) {

        User newUser = user.toBuilder().id(nextId).build();
        nextId++;

        if (isFieldEmpty(user.getName())) { // устанавливаем логин в качестве имени в случае незаполненного поля
            newUser = newUser.toBuilder().name(user.getLogin()).build();
        }

        updateUserProperties(newUser); // сохранение информации о пользователе

        log.info("Сохранен пользователь: {}", newUser);

        return newUser;
    }

    @Override
    public void checkUserId(Long userId) {
        if (!users.containsKey(userId)) {
            throw new ObjectNotFoundException(String.format("Пользователь с id: %d не найден", userId));
        }
    }

    // обновление информации о пользователе
    @Override
    public User updateUser(User user) {

        checkUserId(user.getId());
        User updatedUser;

        if (isFieldEmpty(user.getName())) {
            updatedUser = user.toBuilder().name(user.getLogin()).build();
            // устанавливаем логин в качестве имени в случае незаполненного поля
        } else {
            updatedUser = user;
        }

        updateUserProperties(updatedUser); // обновление информации о пользователе

        log.info("Обновлены данные пользователя {}", updatedUser);
        return updatedUser;

    }

    // получение списка пользователей
    @Override
    public List<User> listUsers() {

        List<User> listUsers = new ArrayList<>(users.values());

        log.info("Количество пользователей в списке: {}", listUsers.size());

        return listUsers;

    }

    @Override
    public User getUserById(Long userId) { // получение пользователя по идентификатору

        return users.values().stream()
                .filter(user -> user.getId().equals(userId))
                .findFirst()
                .orElseThrow(() ->
                        new ObjectNotFoundException(String.format("Пользователь с id: %d не найден", userId)));
    }

    public void updateUserProperties(User user) { // сохранение новой или обновленной информации о пользователе
        users.put(user.getId(), user);
    }


    private boolean isFieldEmpty(String fieldValue) { // проверка является ли поле пустым
        return fieldValue == null || fieldValue.isBlank();
    }

}
