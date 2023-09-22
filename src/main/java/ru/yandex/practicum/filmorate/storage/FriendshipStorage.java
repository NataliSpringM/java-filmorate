package ru.yandex.practicum.filmorate.storage;

import java.util.Set;

import org.springframework.stereotype.Repository;
/**
 *  хранение информации о дружбе пользователей
 */

public interface FriendshipStorage {

	/**
	 *  добавление пользователя в список друзей в UserStorage
	 * @param userId
	 * @param friendId
	 */
    void addFriend(Long userId, Long friendId);

    /**
     *  удаление пользователя из списка друзей в UserStorage
     * @param userId
     * @param friendId
     */
    void deleteFriend(Long userId, Long friendId);

    /**
     *  получение списка друзей пользователя из UserStorage
     * @param id
     * @return
     */
    Set<Long> listUserFriends(Long id);

    /**
     *  получение списка общих друзей
     * @param userId
     * @param otherId
     * @return
     */
    Set<Long> listCommonFriends(Long userId, Long otherId);

    /**
     *  проверка взаимности дружбы
     * @param userId
     * @param friendId
     * @return
     */
    Boolean isFriendshipConfirmed(Long userId, Long friendId);

}
