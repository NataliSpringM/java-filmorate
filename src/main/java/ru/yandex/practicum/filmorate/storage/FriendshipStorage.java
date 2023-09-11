package ru.yandex.practicum.filmorate.storage;

import java.util.Set;

public interface FriendshipStorage {

    // хранение информации о дружбе пользователей

    void addFriend(Long userId, Long friendId); //добавление пользователя в список друзей в UserStorage

    void deleteFriend(Long userId, Long friendId);  // удаление пользователя из списка друзей в UserStorage

    Set<Long> listUserFriends(Long id); // получение списка друзей пользователя из UserStorage

    Set<Long> listCommonFriends(Long userId, Long otherId); // получение списка общих друзей

    Boolean isFriendshipConfirmed(Long userId, Long friendId); // проверка взаимности дружбы

}
