package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;


// реализация сервиса для обработки запросов на создание / удаление / получение списков друзей пользователя
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;
    private final FriendshipStorage friendshipStorage;

    // добавление информации о пользователе
    @Override
    public User addUser(User user) {

        return userStorage.addUser(user);
    }

    // обновление информации о пользователе
    @Override
    public User updateUser(User user) {

        return userStorage.updateUser(user);
    }

    // получение списка пользователей
    @Override
    public List<User> listUsers() {

        return userStorage.listUsers();
    }

    // получение пользователя по id
    @Override
    public User getUserById(Long id) {

        return userStorage.getUserById(id);
    }

    // добавление друга
    @Override
    public void addFriend(Long userId, Long friendId) {

        userStorage.checkUserId(userId);
        userStorage.checkUserId(friendId);

        friendshipStorage.addFriend(userId, friendId);

    }


    // удаление из друзей
    @Override
    public void deleteFriend(Long userId, Long friendId) {


        userStorage.checkUserId(userId);
        userStorage.checkUserId(friendId);

        friendshipStorage.deleteFriend(userId, friendId);

    }

    // получение списка друзей пользователя
    @Override
    public List<User> listUserFriends(Long userId) {

        userStorage.checkUserId(userId);
        Set<Long> friendsId = friendshipStorage.listUserFriends(userId);

        return convertIdSetToUserList(friendsId);

    }

    // получение списка общих друзей пользователей
    @Override
    public List<User> listCommonFriends(Long userId, Long otherId) {

        userStorage.checkUserId(userId);
        userStorage.checkUserId(otherId);

        Set<Long> commonFriendsId = friendshipStorage.listCommonFriends(userId, otherId);

        return convertIdSetToUserList(commonFriendsId);

    }

    // получение информации о взаимности дружбы пользователей
    @Override
    public Boolean isFriendShipConfirmed(Long userId, Long friendId) {

        return friendshipStorage.isFriendshipConfirmed(userId, friendId);
    }

    @Override
	public boolean delete(Integer id) {
    	return userStorage.delete(id);
	}

	@Override
	public void clearAll() {
		userStorage.clearAll();
	}

    // преобразование набора id в список пользователей
    private List<User> convertIdSetToUserList(Set<Long> set) {

        return set.stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());

    }
}



