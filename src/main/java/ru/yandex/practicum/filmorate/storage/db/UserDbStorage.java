package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.mappers.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


/**
 * реализация сохранения и получения информации о пользователях в базе данных
 */

@Slf4j
@Repository
@RequiredArgsConstructor
@Primary
public class UserDbStorage implements UserStorage {

    private final UserMapper userMapper;
    private final JdbcTemplate jdbcTemplate;

    /**
     * добавление нового пользователя
     */
    @Override
    public User addUser(User user) {

        // вставляем данные пользователя в базу данных и получаем сгенерированный id
        SimpleJdbcInsert userInsertion = new SimpleJdbcInsert(jdbcTemplate).withTableName("users")
                .usingGeneratedKeyColumns("user_id");

        Long userId = userInsertion.executeAndReturnKey(user.toMap()).longValue();

        // возвращаем данные пользователя с присвоенным id
        User newUser = getUserById(userId);

        log.info("Создан пользователь: {} ", newUser);

        return newUser;
    }

    /**
     * проверка сущестования id пользователя в базе данных
     */
    @Override
    public void checkUserId(Long userId) {

        SqlRowSet sqlUser = jdbcTemplate.queryForRowSet("SELECT user_id FROM users WHERE user_id = ?", userId);

        if (!sqlUser.next()) {
            log.info("Пользователь с идентификатором {} не найден.", userId);
            throw new ObjectNotFoundException(String.format("Пользователь с id: %d не найден", userId));
        }

    }

    /**
     * обновление данных существующего пользователя
     */
    @Override
    public User updateUser(User user) {

        // проверка существования пользователя в базе данных
        checkUserId(user.getId());
        // обновляем данные пользователя в базе данных
        updateUserProperties(user);

        // получение обновленных данных
        User updatedUser = getUserById(user.getId());
        log.info("Обновлен пользователь: {} ", updatedUser);

        return updatedUser;

    }

    // запрашиваем данные всех пользователей
    @Override
    public List<User> listUsers() {

        String sqlUser = "SELECT * FROM users";
        List<User> users = jdbcTemplate.query(sqlUser,
                userMapper);
        logResultList(users);
        return users;
    }

    // получение данных пользователя по id
    @Override
    public User getUserById(Long userId) {

        checkUserId(userId);
        String sql = "SELECT * FROM users WHERE user_id = ?";
        User user = jdbcTemplate.queryForObject(sql, userMapper, userId);
        log.info("Найден пользователь: {} ", user);
        return user;

    }

    // получение списка друзей пользователя
    @Override
    public List<User> listUserFriends(Long userId) {

        String sql = "SELECT * FROM users " +
                "WHERE user_id IN(SELECT recipient_id FROM friendship WHERE initiator_id = ?)";
        List<User> friends = jdbcTemplate.query(sql, userMapper, userId);
        logResultList(friends);
        return friends;
    }

    // получение списка общих друзей
    @Override
    public List<User> listCommonFriends(Long userId, Long otherId) {

        String sql = "SELECT * FROM users WHERE user_id IN "
                + "(SELECT recipient_id FROM friendship WHERE initiator_id = ? AND recipient_id IN " +
                "(SELECT recipient_id FROM friendship WHERE initiator_id = ?))";
        List<User> commonFriends = jdbcTemplate.query(sql, userMapper, userId, otherId);
        log.info("Общих друзей в списке у пользователей {} и {} : {}", userId, otherId, commonFriends.size());
        logResultList(commonFriends);
        return commonFriends;

    }

    /**
     * обновление данных о пользователе в таблице users
     */
    @Override
    public void updateUserProperties(User user) {

        String sqlQueryUser = "UPDATE users SET "
                + "email = ?, user_name = ?, login = ?, birthday = ? "
                + "WHERE user_id = ?";
        String name = (user.getName() == null || user.getName().isBlank()) ? user.getLogin() : user.getName();

        jdbcTemplate.update(sqlQueryUser, user.getEmail(), name, user.getLogin(), user.getBirthday(), user.getId());
    }


    /**
     * Удаление данных о пользователях в таблице users
     */
    @Override
    public void clearAll() {
        jdbcTemplate.execute("delete from users");
    }

    // Удаление данных о пользователе в таблице users
    @Override
    public boolean delete(Integer id) {

        this.checkUserId(Long.valueOf(id));
        String sql = "DELETE FROM users WHERE user_id = ?";
        jdbcTemplate.update(sql, id);
        return true;

    }

    /**
     * логирование списка
     */
    private void logResultList(Collection<User> users) {

        String result = users.stream()
                .map(User::toString)
                .collect(Collectors.joining(", "));

        log.info("Список пользователей по запросу: {}", result);

    }

}
