package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.FriendshipStorage;

import java.util.HashSet;
import java.util.Set;

/**
 * реализация сохранения и получения информации о друзьях пользователей в базе
 * данных
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
        String sqlQueryDeleteFriend = "DELETE FROM friendship WHERE initiator_id = ? AND recipient_id = ?";
        jdbcTemplate.update(sqlQueryDeleteFriend, userId, friendId);
        log.info("Пользователь {} удалил заявку на дружбу с {}", userId, friendId);

        // в случае наличия заявки на дружбы у друга изменяем статус заявки
        if (isFriendshipMutual(userId, friendId)) {

            String sqlQueryChangeStatus = "MERGE INTO friendship (initiator_id, recipient_id, is_confirmed) VALUES (?, ?, ?)";
            jdbcTemplate.update(sqlQueryChangeStatus, friendId, userId, false);
            log.info("Пользователь {} удалил дружбу с {}", userId, friendId);

        }
    }

    /**
     * получение списка id друзей пользователя
     */
    @Override
    public Set<Long> listUserFriendsId(Long userId) {

        SqlRowSet sqlQueryListUserFriends = jdbcTemplate
                .queryForRowSet("SELECT recipient_id FROM friendship WHERE initiator_id = ?",
                        userId);
        Set<Long> friendsId = new HashSet<>();

        while (sqlQueryListUserFriends.next()) {
            friendsId.add(sqlQueryListUserFriends.getLong("recipient_id"));
        }
        return friendsId;
    }


    /**
     * получение информации о взаимном подтверждении дружбы двумя пользователями
     */
    @Override
    public Boolean isFriendshipConfirmed(Long userId, Long friendId) {

        SqlRowSet sqlQueryIsFriendshipConfirmed = jdbcTemplate.queryForRowSet(
                "SELECT is_confirmed FROM friendship " + "WHERE initiator_id = ? AND recipient_id = ?", userId,
                friendId);

        boolean status = false;

        if (sqlQueryIsFriendshipConfirmed.next()) {
            status = sqlQueryIsFriendshipConfirmed.getBoolean("is_confirmed");
        }

        return status;
    }

    /**
     * проверка наличия заявки на дружбу от потенциального друга пользователя
     *
     * @param userId   id пользователя
     * @param friendId id друга
     * @return подтверждение взаимонсти дружбы
     */
    private Boolean isFriendshipMutual(Long userId, Long friendId) {

        SqlRowSet sqlQueryIsFriendshipMutual = jdbcTemplate.queryForRowSet(
                "SELECT * FROM friendship " + "WHERE initiator_id = ? AND recipient_id = ?", friendId, userId);

        return sqlQueryIsFriendshipMutual.next();
    }

}
