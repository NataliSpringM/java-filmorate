package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

// сервис для добавления, удаления, получения списков друзей пользователя

public interface UserService {
    User addUser(User user);  // добавление информации о пользователе

    User updateUser(User user);  // обновление информации о пользователе

    List<User> listUsers(); // получение списка пользователей

    User getUserById(Long id); // получение пользователя по идентификатору

    void addFriend(Long userId, Long friendId); //добавление пользователя в список друзей

    void deleteFriend(Long userId, Long friendId);  // удаление пользователя из списка друзей

    List<User> listUserFriends(Long id); // получение списка друзей пользователя

    List<User> listCommonFriends(Long userId, Long otherId); // получение списка общих друзей пользователей

    Boolean isFriendShipConfirmed(Long userId, Long friendId); // подтверждение взаимности дружбы пользователей

}

