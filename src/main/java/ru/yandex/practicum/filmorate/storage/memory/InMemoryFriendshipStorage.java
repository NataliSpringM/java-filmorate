package ru.yandex.practicum.filmorate.storage.memory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class InMemoryFriendshipStorage implements FriendshipStorage {

// реализация хранения информации о дружбе пользователей в памяти

    private final UserStorage userStorage;
    private final Map<Long, Set<Long>> mutualFriendsMap;

    // добавление друга пользователя
    @Override
    public void addFriend(Long userId, Long friendId) {

        // проверка существования id пользователей, получение списка друзей пользователей
        Set<Long> userFriends = listUserFriends(userId);
        Set<Long> friendFriends = listUserFriends(friendId);

        // добавляем друга пользователю, обновляем информацию о пользователе в хранилище
        userFriends.add(friendId);
        User updatedUser = userStorage.getUserById(userId).toBuilder().friends(userFriends).build();
        userStorage.updateUserProperties(updatedUser);
        log.info("Пользователь {} подал заявку на дружбу с {}", userId, friendId);

        // проверяем наличие заявки на дружбу у потенциального друга
        if (friendFriends.contains(userId)) {

            // сохраняем информацию о взаимной дружбе

            Set<Long> mutualUserFriends = returnEmptyCollectionIfNull(mutualFriendsMap.get(userId));
            Set<Long> mutualFriendFriends = returnEmptyCollectionIfNull(mutualFriendsMap.get(friendId));

            mutualUserFriends.add(friendId);
            mutualFriendFriends.add(userId);
            mutualFriendsMap.put(userId, mutualUserFriends);
            mutualFriendsMap.put(friendId, mutualFriendFriends);

            log.info("Пользователи {} и {} дружат взаимно", userId, friendId);

        }

    }


    // удаление друга пользователя
    @Override
    public void deleteFriend(Long userId, Long friendId) {
        // проверка существования id пользователей, получение списка друзей пользователей
        Set<Long> userFriends = listUserFriends(userId);
        Set<Long> friendFriends = listUserFriends(friendId);

        // удаляем заявку на дружбу у пользователя и обновляем его данные в хранилище
        userFriends.remove(friendId);
        User updatedUser = userStorage.getUserById(userId).toBuilder().friends(userFriends).build();
        userStorage.updateUserProperties(updatedUser);
        log.info("Пользователь {} удалил заявку на дружбу с {}", userId, friendId);

        // проверяем наличие заявки на дружбу у потенциального друга
        if (friendFriends.contains(userId)) {

            // удаляем информацию о взаимной дружбе между пользователями
            Set<Long> mutualUserFriends = returnEmptyCollectionIfNull(mutualFriendsMap.get(userId));
            Set<Long> mutualFriendFriends = returnEmptyCollectionIfNull(mutualFriendsMap.get(friendId));

            mutualUserFriends.remove(friendId);
            mutualFriendFriends.remove(userId);
            mutualFriendsMap.put(userId, mutualUserFriends);
            mutualFriendsMap.put(friendId, mutualFriendFriends);

            log.info("Пользователь {} удалил дружбу с {}", userId, friendId);

        }

    }

    // получение списка друзей пользователя
    @Override
    public Set<Long> listUserFriends(Long userId) {

        User user = userStorage.getUserById(userId);
        Set<Long> userFriends = returnEmptyCollectionIfNull(user.getFriends());

        log.info("У пользователя {} {} друзей в списке", user, userFriends.size());

        return userFriends;

    }

    // получение списка общих друзей пользователя
    @Override
    public Set<Long> listCommonFriends(Long userId, Long otherId) {

        // получаем пользователей по id
        User user = userStorage.getUserById(userId);
        User other = userStorage.getUserById(otherId);

        // получаем списки id друзей
        HashSet<Long> commonFriends = new HashSet<>(returnEmptyCollectionIfNull(user.getFriends()));
        HashSet<Long> friendFriends = new HashSet<>(returnEmptyCollectionIfNull(other.getFriends()));

        // оставляем в коллекции только id общих друзей
        commonFriends.retainAll(friendFriends);

        log.info("Общих друзей в списке у пользователей {} и {} : {}", user, other, commonFriends.size());

        // преобразуем набор id в список пользователей и возвращаем
        return commonFriends;
    }

    // получение информации о наличии взаимной дружбы между пользователями
    public Boolean isFriendshipConfirmed(Long userId, Long friendId) {

        return mutualFriendsMap.containsKey(userId) && mutualFriendsMap.get(userId).contains(friendId);
    }


    // проверка на null и возврат пустой коллекции
    private Set<Long> returnEmptyCollectionIfNull(Set<Long> set) {

        return (set == null) ? new HashSet<>() : set;
    }


}
