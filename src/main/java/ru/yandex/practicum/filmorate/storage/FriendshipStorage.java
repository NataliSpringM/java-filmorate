package ru.yandex.practicum.filmorate.storage;

import java.util.Set;

/**
 * хранение информации о дружбе пользователей
 */

public interface FriendshipStorage {

    /**
     * добавление пользователя в список друзей в UserStorage
     *
     * @param userId   id пользователя
     * @param friendId id друга
     */
    void addFriend(Long userId, Long friendId);

    /**
     * удаление пользователя из списка друзей в UserStorage
     *
     * @param userId   id пользователя
     * @param friendId id друга
     */
    void deleteFriend(Long userId, Long friendId);


    /**
     * список id друзей пользователя
     *
     * @param id id пользователя
     * @return список id
     */
    Set<Long> listUserFriendsId(Long id); // получение списка id  друзей пользователя


    /**
     * проверка взаимности дружбы
     *
     * @param userId   id пользователя
     * @param friendId id потенциального друга
     * @return подтверждение является ли дружба взаимной
     */
    Boolean isFriendshipConfirmed(Long userId, Long friendId);

}
