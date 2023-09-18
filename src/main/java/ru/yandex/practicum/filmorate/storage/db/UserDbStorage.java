package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.ResultSet;
import java.util.*;

@Slf4j
@Repository
@RequiredArgsConstructor
@Primary
public class UserDbStorage implements UserStorage {

    // реализация сохранения и получения информации о пользователях в базе данных
    private final RowMapper<Film> filmMapper;
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

    public boolean checkIsObjectInStorage(Long user) {
        String sqlQuery = "SELECT EXISTS (SELECT 1 FROM users WHERE user_id = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sqlQuery, Boolean.class, user));
    }

    @Override
    public List<Film> getRecommendation(Long userId) {
        String sqlQuery = "SELECT fl1.user_id " +
                "FROM films_likes AS fl1 " +
                "LEFT JOIN films_likes AS fl2 " +
                "ON fl1.user_id = fl2.user_id " +
                "WHERE fl1.film_id IN (SELECT film_id FROM films_likes WHERE user_id = ?) " +
                "AND fl1.user_id <> ? " +
                "GROUP BY fl1.user_id  " +
                "ORDER BY COUNT (fl1.film_id) DESC, COUNT (fl2.film_id)  DESC LIMIT 1 ";
        Integer optimalUser = jdbcTemplate.queryForObject(sqlQuery,
                (ResultSet resultSet, int rowNum) -> resultSet.getInt("user_id"), userId, userId);
        if (!(optimalUser == null)) {
            String sqlQuery2 = "SELECT * " +
                    "FROM films_likes AS fl LEFT JOIN films AS f " +
                    "ON fl.film_id = f.film_id " +
                    "WHERE fl.user_id = ? " +
                    "AND fl.film_id NOT IN (SELECT film_id FROM films_likes WHERE user_id = ?)";
            return jdbcTemplate.query(sqlQuery2, filmMapper, optimalUser, userId);
        } else {
            return Collections.emptyList();
        }
    }
}





