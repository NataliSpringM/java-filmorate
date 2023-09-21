package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

//хранение информации о пользователях

public interface UserStorage {

    User addUser(User user);  // добавление информации о пользователе

    User updateUser(User user);  // обновление информации о пользователе

    List<User> listUsers(); // получение списка пользователей

    User getUserById(Long id); // получение пользователя по идентификатору

    void updateUserProperties(User user); // сохранение новой или обновленной информации о пользователе

    void checkUserId(Long userId); // проверка существования id пользователя

	void clearAll(); // удаление всех пользователей

	boolean delete(Integer id); // удаление пользователя по id

}
