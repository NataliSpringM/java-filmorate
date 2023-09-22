package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.FriendshipStorage;

import java.util.*;
/**
 *  реализация сохранения и получения информации о друзьях пользователей в базе данных
 */
@Slf4j
@RequiredArgsConstructor
@Repository
@Primary
public class FriendshipDbStorage implements FriendshipStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addFriend(Long userId, Long friendId) {

        String sqlQueryAddFriend = "MERGE INTO friendship (initiator_id, recipient_id, is_confirmed) VALUES (?, ?, ?)";

        // проверяем наличие заявки на дружбу с пользователем у потенциального друга
        if (isFriendshipMutual(userId, friendId)) {
            // добавляем подтверждение дружбы обоим пользователям
            jdbcTemplate.update(sqlQueryAddFriend, userId, friendId, true);
            jdbcTemplate.update(sqlQueryAddFriend, friendId, userId, true);
            log.info("Пользователи {} и {} дружат взаимно", userId, friendId);

        } else {
            // добавляем пользователю заявку на дружбу
            jdbcTemplate.update(sqlQueryAddFriend, userId, friendId, false);
            log.info("Пользователь {} подал заявку на дружбу с {}", userId, friendId);
        }

    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {

        // удаляем у пользователя заявку на дружбу
        String sqlQueryDeleteFriend =
                "DELETE FROM friendship WHERE initiator_id = ? AND recipient_id = ?";
        jdbcTemplate.update(sqlQueryDeleteFriend, userId, friendId);
        log.info("Пользователь {} удалил заявку на дружбу с {}", userId, friendId);

        // в случае наличия заявки на дружбы у друга изменяем статус заявки
        if (isFriendshipMutual(userId, friendId)) {

            String sqlQueryChangeStatus =
                    "MERGE INTO friendship (initiator_id, recipient_id, is_confirmed) VALUES (?, ?, ?)";
            jdbcTemplate.update(sqlQueryChangeStatus, friendId, userId, false);
            log.info("Пользователь {} удалил дружбу с {}", userId, friendId);

        }
    }

    /**
     *  получение списка друзей пользователя
     */
    @Override
    public Set<Long> listUserFriends(Long userId) {

        SqlRowSet sqlQueryListUserFriends = jdbcTemplate
                .queryForRowSet("SELECT recipient_id FROM friendship WHERE initiator_id = ?",
                        userId);

        Set<Long> friends = new HashSet<>();

        while (sqlQueryListUserFriends.next()) {
            friends.add(sqlQueryListUserFriends.getLong("recipient_id"));
        }

        return friends;
    }

    /**
     *  получение списка общих друзей
     */
    @Override
    public Set<Long> listCommonFriends(Long userId, Long otherId) {

        SqlRowSet sqlQueryListCommonFriends = jdbcTemplate
                .queryForRowSet("SELECT f1.recipient_id FROM friendship AS f1 JOIN friendship AS f2 "
                        + "ON f1.recipient_id = f2.recipient_id "
                        + "WHERE f1.initiator_id = ? AND f2.initiator_id = ?", userId, otherId);

        Set<Long> friends = new HashSet<>();

        while (sqlQueryListCommonFriends.next()) {
            friends.add(sqlQueryListCommonFriends.getLong("recipient_id"));
        }

        log.info("Общих друзей в списке у пользователей {} и {} : {}", userId, otherId, friends.size());

        return friends;

    }

    /**
     *  получение информации о взаимном подтверждении дружбы двумя пользователями
     */
    @Override
    public Boolean isFriendshipConfirmed(Long userId, Long friendId) {

        SqlRowSet sqlQueryIsFriendshipConfirmed = jdbcTemplate
                .queryForRowSet("SELECT is_confirmed FROM friendship "
                        + "WHERE initiator_id = ? AND recipient_id = ?", userId, friendId);

        boolean status = false;

        if (sqlQueryIsFriendshipConfirmed.next()) {
            status = sqlQueryIsFriendshipConfirmed.getBoolean("is_confirmed");
        }

        return status;
    }

    /**
     *  проверка наличия заявки на дружбу от потенциального друга пользователя
     * @param userId
     * @param friendId
     * @return
     */
    private Boolean isFriendshipMutual(Long userId, Long friendId) {

        SqlRowSet sqlQueryIsFriendshipMutual = jdbcTemplate.queryForRowSet("SELECT * FROM friendship "
                + "WHERE initiator_id = ? AND recipient_id = ?", friendId, userId);

        return sqlQueryIsFriendshipMutual.next();
    }


}
