package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

/**
 *  сервис для добавления, удаления, получения списков друзей пользователя
 */
public interface UserService {

	/**
	 *  добавление информации о пользователе
	 * @param user
	 * @return
	 */
    User addUser(User user);

    /**
     *  обновление информации о пользователе
     * @param user
     * @return
     */
    User updateUser(User user);

    /**
     *  получение списка пользователей
     * @return
     */
    List<User> listUsers();

    /**
     *  получение пользователя по идентификатору
     * @param id
     * @return
     */
    User getUserById(Long id);

    /**
     * добавление пользователя в список друзей
     * @param userId
     * @param friendId
     */
    void addFriend(Long userId, Long friendId);

    /**
     *  удаление пользователя из списка друзей
     * @param userId
     * @param friendId
     */
    void deleteFriend(Long userId, Long friendId);

    /**
     *  получение списка друзей пользователя
     * @param id
     * @return
     */
    List<User> listUserFriends(Long id);

    /**
     *  получение списка общих друзей пользователей
     * @param userId
     * @param otherId
     * @return
     */
    List<User> listCommonFriends(Long userId, Long otherId);

    /**
     *  подтверждение взаимности дружбы пользователей
     * @param userId
     * @param friendId
     * @return
     */
    Boolean isFriendShipConfirmed(Long userId, Long friendId);

}

