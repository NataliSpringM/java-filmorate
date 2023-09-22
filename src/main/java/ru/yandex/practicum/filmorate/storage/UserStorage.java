package ru.yandex.practicum.filmorate.storage;

import java.util.List;

import ru.yandex.practicum.filmorate.model.User;

/**
 * хранение информации о пользователях
 */

public interface UserStorage {

    /**
     * добавление информации о пользователе
     *
     * @param user объект User
     * @return объект User с id
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
     * @param id id пользователя
     * @return объект User
     */
    User getUserById(Long id);

    /**
     * сохранение новой или обновленной информации о пользователе
     *
     * @param user объект User
     */
    void updateUserProperties(User user);

    /**
     * проверка существования id пользователя
     *
     * @param userId id пользователя
     */
    void checkUserId(Long userId);

    /**
     * удаление всех пользователей
     */
    void clearAll();

    /**
     * удаление пользователя по id
     *
     * @param id id пользователя
     * @return подтверждение удаления
     */
    boolean delete(Integer id);

    /**
     * получение списка друзей пользователя
     *
     * @param id id пользователя
     * @return список друзей пользователя
     */
    List<User> listUserFriends(Long id);

    /**
     * получение списка общих друзей
     *
     * @param userId  id пользователя
     * @param otherId id второго пользователя
     * @return список общих друзей
     */
    List<User> listCommonFriends(Long userId, Long otherId); // получение списка общих друзей


}
