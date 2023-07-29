package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

// сервис для добавления, удаления, получение списков друзей пользователя
public interface UserService {

    User addFriend(Long userId, Long friendId); //добавление пользователя в список друзей

    User deleteFriend(Long userId, Long friendId);  // удаление пользователя из списка друзей

    List<User> listUserFriends(Long id); // получение списка друзей пользователя

    List<User> listCommonFriends(Long userId, Long otherId); // получение списка общих друзей

}

