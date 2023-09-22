package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collections;
import java.util.List;

/**
 * реализация сервиса для обработки запросов на создание / удаление / получение
 * списков друзей пользователя
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;
    private final FilmStorage filmStorage;
    private final FriendshipStorage friendshipStorage;

    /**
     * добавление информации о пользователе
     */
    @Override
    public User addUser(User user) {

        return userStorage.addUser(user);
    }

    /**
     * обновление информации о пользователе
     */
    @Override
    public User updateUser(User user) {

        return userStorage.updateUser(user);
    }

    /**
     * получение списка пользователей
     */
    @Override
    public List<User> listUsers() {

        return userStorage.listUsers();
    }

    /**
     * получение пользователя по id
     */
    @Override
    public User getUserById(Long id) {

        return userStorage.getUserById(id);
    }

    /**
     * добавление друга
     */
    @Override
    public void addFriend(Long userId, Long friendId) {

        userStorage.checkUserId(userId);
        userStorage.checkUserId(friendId);

        friendshipStorage.addFriend(userId, friendId);
    }

    /**
     * удаление из друзей
     */
    @Override
    public void deleteFriend(Long userId, Long friendId) {

        userStorage.checkUserId(userId);
        userStorage.checkUserId(friendId);

        friendshipStorage.deleteFriend(userId, friendId);
    }

    /**
     * получение списка друзей пользователя
     */
    @Override
    public List<User> listUserFriends(Long userId) {

        userStorage.checkUserId(userId);

        return userStorage.listUserFriends(userId);
    }

    /**
     * получение списка общих друзей пользователей
     */
    @Override
    public List<User> listCommonFriends(Long userId, Long otherId) {

        userStorage.checkUserId(userId);
        userStorage.checkUserId(otherId);

        return userStorage.listCommonFriends(userId, otherId);

    }

    /**
     * получение информации о взаимности дружбы пользователей
     */
    @Override
    public Boolean isFriendShipConfirmed(Long userId, Long friendId) {

        return friendshipStorage.isFriendshipConfirmed(userId, friendId);
    }

    /**
     * удаление пользователя по id
     */
    @Override
    public boolean delete(Integer id) {
        return userStorage.delete(id);
    }

    /**
     * удаление всех пользователей
     */
    @Override
    public void clearAll() {
        userStorage.clearAll();
    }


    /**
     * получение списка рекомендаций
     */
    @Override
    public List<Film> getRecommendation(Long userId) {
        try {
            userStorage.checkUserId(userId);
            List<Film> recommendations = filmStorage.getRecommendation(userId);
            log.info("Получен список рекоммендаций для пользователя user_id =" + userId + ".");
            return recommendations;
        } catch (IncorrectResultSizeDataAccessException e) {
            return Collections.emptyList();
        }
    }
}
