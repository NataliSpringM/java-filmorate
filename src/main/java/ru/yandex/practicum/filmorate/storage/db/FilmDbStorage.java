package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.ResultSet;
import java.util.*;
import java.util.stream.Collectors;

/**
 * реализация сохранения и получения информации о фильмах в базе данных
 */
@Slf4j
@Repository
@RequiredArgsConstructor
@Primary
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final FilmMapper filmMapper;
    @Autowired
    private final DirectorStorage directorStorage;

    /**
     * добавление информации о фильме
     */
    @Override
    public Film addFilm(Film film) {

        // вставляем данные фильма в базу данных и получаем сгенерированный id
        SimpleJdbcInsert filmInsertion = new SimpleJdbcInsert(jdbcTemplate).withTableName("films")
                .usingGeneratedKeyColumns("film_id");

        Integer filmId = filmInsertion.executeAndReturnKey(film.toMap()).intValue();

        // присваиваем id и сохраняем полученные данные
        Film filmWithId = film.toBuilder().id(filmId).build();
        updateFilmGenreTable(filmWithId);
        updateFilmDirectorTable(filmWithId);

        Film newFilm = getFilmById(filmId);
        log.info("Сохранена информация о фильме: {}", newFilm);

        return newFilm;
    }

    /**
     * проверка существования id фильма в базе данных
     */
    @Override
    public void checkFilmId(Integer filmId) {

        SqlRowSet sqlId = jdbcTemplate.queryForRowSet("SELECT film_id FROM films WHERE film_id = ?", filmId);

        if (!sqlId.next()) {
            log.info("Фильм с идентификатором {} не найден.", filmId);
            throw new ObjectNotFoundException(String.format("Фильм с id: %d не найден", filmId));
        }
    }

    /**
     * обновление информации о фильме
     */
    @Override
    public Film updateFilm(Film film) {

        checkFilmId(film.getId());
        updateFilmData(film);
        Film result = getFilmById(film.getId());
        log.info("Обновлена информация о фильме: {}", result);

        return result;
    }

    /**
     * получение списка фильмов
     */
    @Override
    public List<Film> listFilms() {

        StringBuilder sqlBuilder = new StringBuilder()
                .append("SELECT ")
                .append("f.*, r.*, ")
                .append("COUNT(l.user_id) AS total_likes ")
                .append("FROM films AS f ")
                .append("INNER JOIN rating_mpa r ON f.rating_mpa_id = r.rating_mpa_id ")
                .append("LEFT JOIN likes l ON f.film_id = l.film_id ")
                .append("GROUP BY f.film_id ")
                .append("ORDER BY total_likes DESC ");

        String sqlFilm = sqlBuilder.toString();

        List<Film> films = jdbcTemplate.query(sqlFilm, filmMapper);
        logResultList(films);
        return films;
    }

    /**
     * получение информации о фильме по id
     */
    @Override
    public Film getFilmById(Integer filmId) {

        checkFilmId(filmId);

        StringBuilder sqlBuilder = new StringBuilder()
                .append("SELECT ")
                .append("f.*, r.*, ")
                .append("COUNT(l.user_id) AS total_likes ")
                .append("FROM films AS f ")
                .append("INNER JOIN rating_mpa r ON f.rating_mpa_id = r.rating_mpa_id ")
                .append("LEFT JOIN likes l ON f.film_id = l.film_id ")
                .append("WHERE f.film_id = ? ")
                .append("GROUP BY f.film_id");

        String sql = sqlBuilder.toString();
        // создаем объект Film на основе информации из всех необходимых таблиц

        Film film = jdbcTemplate.queryForObject(sql, filmMapper, filmId);
        log.info("Найден фильм: {}", film);
        return film;
    }

    /**
     * обновление информации о фильме
     */
    @Override
    public void updateFilmData(Film film) {

        // обновление информации в таблице films
        updateFilmTable(film);
        // обновление информации в таблице film_genres о жанрах фильма
        updateFilmGenreTable(film);
        // обновление информации в таблице film_directors о режиссерах фильма
        updateFilmDirectorTable(film);

    }

    /**
     * получение списка фильмов по режиссеру
     */
    @Override
    public List<Film> listMostPopularFilms(Integer limit, Integer genreId, Integer year) {

        String genreIdField = "g.genre_id";
        String yearField = "EXTRACT(YEAR FROM CAST(f.release_date AS date))";

        List<Object> conditions = new ArrayList<>();

        StringBuilder sqlBuilder = new StringBuilder()
                .append("SELECT ")
                .append("f.*, r.*, ")
                .append("COUNT(DISTINCT l.user_id) AS total_likes ")
                .append("FROM films AS f ")
                .append("INNER JOIN rating_mpa r ON f.rating_mpa_id = r.rating_mpa_id ")
                .append("LEFT JOIN film_genres fg ON f.film_id = fg.film_id ")
                .append("LEFT JOIN genres g ON fg.genre_id = g.genre_id ")
                .append("LEFT JOIN likes l ON f.film_id = l.film_id ")
                .append("WHERE ");

        if (genreId == null && year == null) {
            sqlBuilder.append("1 = 1");
        } else {
            if (genreId != null && year != null) {
                sqlBuilder.append(genreIdField)
                        .append(" = ?");
                conditions.add(genreId);
                sqlBuilder.append(" AND ")
                        .append(yearField)
                        .append(" = ?");
                conditions.add(year);
            } else if (genreId == null) {
                sqlBuilder.append(yearField)
                        .append(" = ?");
                conditions.add(year);
            } else {
                sqlBuilder.append(genreIdField)
                        .append(" = ?");
                conditions.add(genreId);
            }
        }

        sqlBuilder.append(" GROUP BY f.film_id ")
                .append("ORDER BY total_likes DESC ")
                .append("LIMIT ?");

        conditions.add(limit);

        String sqlFilm = sqlBuilder.toString();
        System.out.println(sqlFilm);

        Object[] conditionsArray = conditions.toArray();
        List<Film> films = jdbcTemplate.query(sqlFilm,
                filmMapper,
                conditionsArray);

        logResultList(films);

        return films;
    }


    /**
     * поиск фильмов по подстроке - по названию фильма / имени режиссера
     */
    @Override
    public List<Film> listSearchResults(String substringQuery, List<String> searchBaseBy) {

        String filmSearch = "title";
        String directorSearch = "director";

        String filmNameField = "f.film_name)";
        String directorNameField = "d.director_name)";
        String caseInsensitive = "LOWER(";

        String condition = " LIKE CONCAT('%', LOWER(?), '%')";

        List<Object> conditions = new ArrayList<>();


        StringBuilder sqlBuilder = new StringBuilder().append("SELECT ")
                .append("f.*, r.*, ")
                .append("COUNT(l.user_id) AS total_likes ").append("FROM films AS f ")
                .append("INNER JOIN rating_mpa r ON f.rating_mpa_id = r.rating_mpa_id ")
                .append("LEFT JOIN film_directors fd ON f.film_id = fd.film_id ")
                .append("LEFT JOIN directors d ON fd.director_id = d.director_id ")
                .append("LEFT JOIN likes l ON f.film_id = l.film_id ")
                .append("WHERE ");

        if (searchBaseBy.contains(filmSearch) && searchBaseBy.contains(directorSearch)) {

            sqlBuilder.append(caseInsensitive).append(filmNameField).append(condition);
            conditions.add(substringQuery);

            sqlBuilder.append(" OR ");

            sqlBuilder.append(caseInsensitive).append(directorNameField).append(condition);
            conditions.add(substringQuery);

        } else if (searchBaseBy.contains(filmSearch)) {
            sqlBuilder.append(caseInsensitive).append(filmNameField).append(condition);
            conditions.add(substringQuery);

        } else if (searchBaseBy.contains(directorSearch)) {
            sqlBuilder.append(caseInsensitive).append(directorNameField).append(condition);
            conditions.add(substringQuery);
        }

        sqlBuilder.append(" GROUP BY f.film_id ")
                .append("ORDER BY total_likes DESC;");

        String sqlFilm = sqlBuilder.toString();

        Object[] conditionsArray = conditions.toArray();

        List<Film> films = jdbcTemplate.query(sqlFilm,
                filmMapper,
                conditionsArray);
        logResultList(films);

        return films;

    }

    /**
     * получает общие фильмы между двумя пользователями на основе их
     * идентификаторов. Он выполняет SQL-запрос к базе данных, возвращая список
     * фильмов, сгруппированных и отсортированных по количеству лайков
     */
    public List<Film> getCommonFilmsBetweenUsers(Long userId, Long friendId) {

        String sqlFilm = "SELECT f.*, r.*, COUNT(l.user_id) AS total_likes "
                + "FROM films AS f "
                + "INNER JOIN rating_mpa AS r ON f.rating_mpa_id = r.rating_mpa_id "
                + "LEFT JOIN likes AS l ON f.film_id = l.film_id "
                + "INNER JOIN likes AS l1 ON f.film_id = l1.film_id AND l1.user_id = ? "
                + "INNER JOIN likes AS l2 ON f.film_id = l2.film_id AND l2.user_id = ? "
                + "GROUP BY f.film_id "
                + "ORDER BY total_likes DESC";

        List<Film> films = jdbcTemplate.query(sqlFilm,
                filmMapper,
                userId, friendId);

        logResultList(films);

        return films;
    }

    // получение списка фильмов по режиссеру
    @Override
    public List<Film> listFilmsOfDirector(Integer directorId) {

        directorStorage.checkDirectorId(directorId);

        String sqlQuery = "SELECT f.*, r.*, COUNT(l.user_id) AS total_likes FROM films f "
                + "LEFT JOIN film_directors fd ON fd.film_id = f.film_id "
                + "JOIN directors d ON fd.director_id = d.director_id "
                + "JOIN rating_mpa r ON f.rating_mpa_id = r.rating_mpa_id "
                + "LEFT JOIN likes AS l ON f.film_id = l.film_id "
                + "WHERE d.director_id = ? "
                + "GROUP BY f.film_id";

        List<Film> films = jdbcTemplate.query(sqlQuery,
                filmMapper,
                directorId);

        logResultList(films);
        return films;
    }

    /**
     * Удаление фильма по id и возвращение boolean успешности операции
     */
    @Override
    public boolean delete(Integer id) {
        this.checkFilmId(id);
        String sql = "DELETE FROM films WHERE film_id = ?";
        jdbcTemplate.update(sql, id);
        return true;
    }

    /**
     * Удаление всех фильмов
     */
    @Override
    public void clearAll() {
        jdbcTemplate.execute("delete from films");
    }


    /**
     * Метод getRecommendation(Long userId) возвращает список рекомендуемых фильмов
     * для пользователя с указанным идентификатором. Для этого метод выполняет два
     * SQL-запроса к базе данных: первый запрос находит пользователя, который
     * совпадает с текущим пользователем по предпочтениям в просмотренных фильмах, а
     * второй запрос находит фильмы, которые были просмотрены этим оптимальным
     * пользователем, но не были просмотрены текущим пользователем. Если оптимальный
     * пользователь не найден, то метод возвращает пустой список.
     *
     * @param userId id пользователя
     * @return dbcTemplate.query(sqlQuery2, filmMapper, optimalUser, userId);
     */
    @Override
    public List<Film> getRecommendation(final Long userId) {
        String sqlQuery = "SELECT fl1.user_id " + "FROM likes AS fl1 " + "LEFT JOIN likes AS fl2 "
                + "ON fl1.user_id = fl2.user_id "
                + "WHERE fl1.film_id IN (SELECT film_id FROM likes WHERE user_id = ?) " + "AND fl1.user_id <> ? "
                + "GROUP BY fl1.user_id  " + "ORDER BY COUNT (fl1.film_id) DESC, COUNT (fl2.film_id)  DESC LIMIT 1 ";
        Integer optimalUser = jdbcTemplate.queryForObject(sqlQuery,
                (ResultSet resultSet, int rowNum) -> resultSet.getInt("user_id"), userId, userId);
        if (!(optimalUser == null)) {
            String sqlQuery2 = "SELECT COUNT(fl.user_id) AS total_likes, r.*, f.*  "
                    + "FROM likes AS fl LEFT JOIN films AS f " + "ON fl.film_id = f.film_id "
                    + "JOIN rating_mpa r ON f.rating_mpa_id = r.rating_mpa_id "
                    + "WHERE fl.user_id = ? "
                    + "AND fl.film_id NOT IN (SELECT film_id FROM likes WHERE user_id = ?)"
                    + "GROUP BY f.film_id";
            return jdbcTemplate.query(sqlQuery2, filmMapper, optimalUser, userId);
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * обновление информации в таблице films
     *
     * @param film объект Film
     */
    private void updateFilmTable(Film film) {

        String sqlQueryFilm = "UPDATE films SET "
                + "film_name =  ?, description = ?, release_date = ?,  duration = ?, rating_mpa_id = ?"
                + "WHERE film_id = ?";

        jdbcTemplate.update(sqlQueryFilm,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
    }

    /**
     * обновление информации в таблице film_genres
     *
     * @param film объект Film
     */
    private void updateFilmGenreTable(Film film) {
        // получаем жанры фильма
        Set<FilmGenre> genres = film.getGenres();

        // обновляем, либо удаляем данные в таблице
        if (genres != null && !genres.isEmpty()) {

            // получаем информацию о новых id жанров фильма, сравниваем с текущими данными
            Set<Integer> newGenresId = genres.stream().map(FilmGenre::getId).collect(Collectors.toSet());
            Set<Integer> oldGenresId = new HashSet<>();

            SqlRowSet oldGenres = jdbcTemplate
                    .queryForRowSet("SELECT * FROM film_genres WHERE film_id = ?", film.getId());

            while (oldGenres.next()) {
                oldGenresId.add(oldGenres.getInt("genre_id"));
            }

            // выбираем данные для удаления
            oldGenresId.removeAll(newGenresId);

            // удаляем необходимые данные
            String sqlQueryDel = "DELETE FROM film_genres WHERE genre_id = ? AND film_id = ?";
            oldGenresId.forEach(genreId -> jdbcTemplate.update(sqlQueryDel, genreId, film.getId()));

            // дополняем данные
            String sqlQueryMerge = "MERGE INTO film_genres (film_id, genre_id) VALUES (?, ?)";
            genres.stream().map(FilmGenre::getId)
                    .forEach(genreId -> jdbcTemplate.update(sqlQueryMerge, film.getId(), genreId));

            log.info("Обновлена информация о жанрах у фильма {}", film.getId());


        } else { // удаление всех данных о жанрах в случае пустого списка

            String sqlQueryDeleteGenres = "DELETE FROM film_genres WHERE film_id = ?";

            jdbcTemplate.update(sqlQueryDeleteGenres, film.getId());

            log.info("Удалена информация о жанрах у фильма {}", film.getId());

        }
    }


    /**
     * обновление информации в таблице film_directors
     *
     * @param film объект Film
     */
    private void updateFilmDirectorTable(Film film) {

        // получаем список режиссеров из объекта
        Set<Director> directors = film.getDirectors();
        Set<Integer> newDirectorIds;

        // получаем список id режиссеров из объекта
        if (directors != null) {
            newDirectorIds = directors.stream().map(Director::getId).collect(Collectors.toSet());
        } else {
            newDirectorIds = new HashSet<>();
        }

        // получаем список id режиссеров из базы
        Set<Integer> oldDirectorIds = new HashSet<>();

        SqlRowSet oldDirectors = jdbcTemplate
                .queryForRowSet("SELECT * FROM film_directors WHERE film_id = ?", film.getId());

        while (oldDirectors.next()) {
            oldDirectorIds.add(oldDirectors.getInt("director_id"));
        }

        // получаем режиссеров, которых есть в базе, но нет в новом списке
        // их необходимо удалить из базы
        Set<Integer> forRemovingIds = new HashSet<>(oldDirectorIds);      // копируем set
        forRemovingIds.removeAll(newDirectorIds);

        // получаем режиссеров фильма, которых нет в базе, но есть в списке
        // их необходимо добавить в базу
        Set<Integer> forInsertionIds = new HashSet<>(newDirectorIds);     // копируем set
        forInsertionIds.removeAll(oldDirectorIds);

        // удаляем из базы режиссеров, которых нет в новом списке
        String sqlQueryDel = "DELETE FROM film_directors WHERE director_id = ? AND film_id = ?;";
        forRemovingIds.forEach(directorId -> jdbcTemplate.update(sqlQueryDel, directorId, film.getId()));

        // из списка добавляем в базу режиссеров, которых раньше не было в базе
        String sqlQueryInsert = "INSERT INTO film_directors (film_id, director_id) VALUES (?, ?);";
        forInsertionIds.forEach(directorId -> jdbcTemplate.update(sqlQueryInsert, film.getId(), directorId));

        log.info("Обновлена информация о режиссерах для фильма {}", film.getId());
    }

    // логирование списка
    private void logResultList(List<Film> films) {

        String result = films.stream()
                .map(Film::toString)
                .collect(Collectors.joining(", "));

        log.info("Список фильмов по запросу: {}.", result);

    }


}
