package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Objects;

@Slf4j
@Repository
@RequiredArgsConstructor
@Primary
public class UserDbStorage implements UserStorage {

    // реализация сохранения и получения информации о пользователях в базе данных
    private final FriendshipDbStorage friendDbStorage;
    private final JdbcTemplate jdbcTemplate;

    // добавление нового пользователя
    @Override
    public User addUser(User user) {

        // вставляем данные пользователя в базу данных и получаем сгенерированный id
        SimpleJdbcInsert userInsertion = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");

        Long userId = userInsertion.executeAndReturnKey(user.toMap()).longValue();

        // возвращаем данные пользователя с присвоенным id
        User newUser = getUserById(userId);

        log.info("Создан пользователь: {} ", newUser);

        return newUser;
    }

    // проверка сущестования id пользователя в базе данных
    @Override
    public void checkUserId(Long userId) {

        SqlRowSet sqlUser = jdbcTemplate.queryForRowSet("SELECT user_id FROM users WHERE user_id = ?", userId);

        if (!sqlUser.next()) {

            log.info("Пользователь с идентификатором {} не найден.", userId);
            throw new ObjectNotFoundException(String.format("Пользователь с id: %d не найден", userId));

        }

    }

    // обновление данных существующего пользователя
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

    @Override
    public List<User> listUsers() {

        // запрашиваем данные всех пользователей
        String sqlUser = "SELECT * FROM users";

        // обрабатываем запрос и возвращаем список объектов пользователей
        return jdbcTemplate.query(sqlUser, (rs, rowNum) -> new User(
                rs.getLong("user_id"),
                rs.getString("email"),
                rs.getString("user_name"),
                rs.getString("login"),
                rs.getDate("birthday").toLocalDate(),
                friendDbStorage.listUserFriends(rs.getLong("user_id"))));
    }

    @Override
    public User getUserById(Long userId) {

        // выполняем запрос к базе данных
        SqlRowSet sqlUser = jdbcTemplate.queryForRowSet("SELECT * FROM users WHERE user_id = ?", userId);
        User user;
        // создание объекта пользователя из таблиц, включающих данные о пользователе
        if (sqlUser.next()) {
            user = new User(sqlUser.getLong("user_id"),
                    sqlUser.getString("email"),
                    sqlUser.getString("user_name"),
                    sqlUser.getString("login"),
                    Objects.requireNonNull(sqlUser.getDate("birthday")).toLocalDate(),
                    friendDbStorage.listUserFriends(sqlUser.getLong("user_id")));
        } else {

            // сообщение об ошибке и выброс исключения при остутствии пользователя в базе данных
            log.info("Пользователь с идентификатором {} не найден.", userId);
            throw new ObjectNotFoundException(String.format("Пользователь с id: %d не найден", userId));

        }

        log.info("Найден пользователь: {} ", user);
        return user;

    }


    // обновление данных о пользователе в таблице users
    @Override
    public void updateUserProperties(User user) {

        String sqlQueryUser = "UPDATE users SET "
                + "email = ?, user_name = ?, login = ?, birthday = ? "
                + "WHERE user_id = ?";

        String name = (user.getName() == null || user.getName().isBlank()) ? user.getLogin() : user.getName();

        jdbcTemplate.update(sqlQueryUser,
                user.getEmail(),
                name,
                user.getLogin(),
                user.getBirthday(),
                user.getId());
    }

    // Удаление данных о пользователях в таблице users
    @Override
    public void clearAll() {
        jdbcTemplate.execute("delete from users");
    }

    // Удаление данных о пользователях в таблице users
    @Override
    public boolean delete(Integer id) {
        boolean status = false;
        this.checkUserId(Long.valueOf(id));
        jdbcTemplate.execute("delete from users where user_id = " + id);
        status = true;
        return status;
    }


}





