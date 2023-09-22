package ru.yandex.practicum.filmorate.service;

import java.util.List;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

/**
 * сервис для добавления, удаления, получения списков друзей пользователя
 */
public interface UserService {

    /**
     * добавление информации о пользователе
     *
     * @param user объект User
     * @return объект User с ic
     */
    User addUser(User user);

    /**
     * обновление информации о пользователе
     *
     * @param user объект User
     * @return обновленный объект User
     */
    User updateUser(User user);

    /**
     * получение списка пользователей
     *
     * @return список пользователей
     */
    List<User> listUsers();

    /**
     * получение пользователя по идентификатору
     *
     * @param id id друга
     * @return объект User
     */
    User getUserById(Long id);

    /**
     * добавление пользователя в список друзей
     *
     * @param userId   id пользователя
     * @param friendId id друга
     */
    void addFriend(Long userId, Long friendId);

    /**
     * удаление пользователя из списка друзей
     *
     * @param userId   id пользователя
     * @param friendId id друга
     */
    void deleteFriend(Long userId, Long friendId);

    /**
     * получение списка друзей пользователя
     *
     * @param id id пользователя
     * @return список друзей
     */
    List<User> listUserFriends(Long id);

    /**
     * получение списка общих друзей пользователей
     *
     * @param userId  id пользователя
     * @param otherId id другого пользователя
     * @return список общих друзей
     */
    List<User> listCommonFriends(Long userId, Long otherId);

    /**
     * подтверждение взаимности дружбы пользователей
     *
     * @param userId   id пользователя
     * @param friendId id друга
     * @return подтверждение взаимности дружбы
     */
    Boolean isFriendShipConfirmed(Long userId, Long friendId);

    /**
     * Получение списка рекомендаций
     *
     * @param id пользователя
     * @return список рекомендаций
     */
    List<Film> getRecommendation(Long id);

    /**
     * удаление пользователя по id
     *
     * @param id id пользователя
     * @return подтверждение удаления
     */
    boolean delete(Integer id); // Удаление id

    /**
     * удаление всех пользователей
     */
    void clearAll();

}
