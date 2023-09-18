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
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Repository
@RequiredArgsConstructor
@Primary
public class FilmDbStorage implements FilmStorage {

    // реализация сохранения и получения информации о фильмах в базе данных

    private final JdbcTemplate jdbcTemplate;
    private final LikeDbStorage likeStorage;
    private final RatingMpaDbStorage mpaStorage;
    private final FilmGenreDbStorage filmGenresStorage;
    @Autowired
    private final DirectorStorage directorStorage;

    // добавление информации о фильме
    @Override
    public Film addFilm(Film film) {

        // вставляем данные фильма в базу данных и получаем сгенерированный id
        SimpleJdbcInsert filmInsertion = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
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

    // проверка существования id фильма в базе данных
    @Override
    public void checkFilmId(Integer filmId) {


        SqlRowSet sqlId = jdbcTemplate.queryForRowSet("SELECT film_id FROM films WHERE film_id = ?", filmId);

        if (!sqlId.next()) {

            log.info("Фильм с идентификатором {} не найден.", filmId);
            throw new ObjectNotFoundException(String.format("Фильм с id: %d не найден", filmId));

        }
    }

    // обновление информации о фильме
    @Override
    public Film updateFilm(Film film) {

        checkFilmId(film.getId());
        updateFilmData(film);

        Film result = getFilmById(film.getId());
        log.info("Обновлена информация о фильме: {}", result);

        return result;
    }

    // получение списка фильмов
    @Override
    public List<Film> listFilms() {

        String sqlFilm = "SELECT * FROM films";

        return jdbcTemplate.query(sqlFilm, (rs, rowNum) -> new Film(
                rs.getInt("film_id"),
                rs.getString("film_name"),
                rs.getString("description"),
                rs.getDate("release_date").toLocalDate(),
                rs.getInt("duration"),
                likeStorage.getFilmLikesTotalCount(rs.getInt("film_id")),
                mpaStorage.getRatingMpaById(rs.getInt("rating_mpa_id")),
                getFilmGenres(rs.getInt("film_id")),
                directorStorage.getDirectorsByFilmId(rs.getInt("film_id"))
                ));
    }


    // получение информации о фильме по id
    @Override
    public Film getFilmById(Integer filmId) {

        checkFilmId(filmId);

        // получение информации из таблицы films
        SqlRowSet sqlFilm = jdbcTemplate.queryForRowSet("SELECT * FROM films WHERE film_id = ?", filmId);

        // создаем объект Film на основе информации из всех необходимых таблиц
        Film film;
        if (sqlFilm.next()) {
            film = new Film(
                    sqlFilm.getInt("film_id"),
                    sqlFilm.getString("film_name"),
                    sqlFilm.getString("description"),
                    Objects.requireNonNull(sqlFilm.getDate("release_date")).toLocalDate(),
                    sqlFilm.getInt("duration"),
                    likeStorage.getFilmLikesTotalCount(sqlFilm.getInt("film_id")),
                    getMpa(filmId),
                    getFilmGenres(filmId),
                    directorStorage.getDirectorsByFilmId(filmId));
        } else {

            log.info("Фильм с идентификатором {} не найден.", filmId);
            throw new ObjectNotFoundException(String.format("Фильм с id: %d не найден", filmId));

        }
        log.info("Найден фильм: {}", film);
        return film;

    }

    // обновление информации о фильме

    @Override
    public void updateFilmData(Film film) {

        // обновление информации в таблице films
        updateFilmTable(film);

        // обновление информации в таблице film_genres о жанрах фильма
        updateFilmGenreTable(film);
        // обновление информации в таблице film_directors о режиссерах фильма
        updateFilmDirectorTable(film);

    }
    // получение списка наиболее популярных фильмов с возможным ограничением размера списка

    @Override
    public List<Film> listMostPopularFilms(Integer limit) {

        String query = "SELECT f.*, COUNT(l.user_id) AS quantity " +
                "FROM films AS f " +
                "LEFT JOIN likes AS l ON f.film_id = l.film_id " +
                "GROUP BY f.film_id " +
                "ORDER BY quantity DESC " +
                "LIMIT ?";

        return jdbcTemplate.query(query, (rs, rowNum) -> new Film(
                rs.getInt("film_id"),
                rs.getString("film_name"),
                rs.getString("description"),
                rs.getDate("release_date").toLocalDate(),
                rs.getInt("duration"),
                likeStorage.getFilmLikesTotalCount(rs.getInt("film_id")),
                mpaStorage.getRatingMpaById(rs.getInt("rating_mpa_id")),
                getFilmGenres(rs.getInt("film_id")),
                directorStorage.getDirectorsByFilmId(rs.getInt("film_id"))), limit);

    }

    // получение списка фильмов по режиссеру
    @Override
    public List<Film> listFilmsOfDirector(Integer directorId) {
        directorStorage.checkDirectorId(directorId);
        /*
        SQL query:
        SELECT *
        FROM (SELECT film_id FROM film_directors WHERE director_id = ?) AS ids
        LEFT JOIN films ON ids.film_id = films.film_id;
         */
        String sqlQuery =
                "SELECT * "
                + " FROM (SELECT film_id FROM film_directors WHERE director_id = ?) AS ids "
                + " LEFT JOIN films ON ids.film_id = films.film_id;";

        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> new Film(
                rs.getInt("ids.film_id"),
                rs.getString("film_name"),
                rs.getString("description"),
                rs.getDate("release_date").toLocalDate(),
                rs.getInt("duration"),
                likeStorage.getFilmLikesTotalCount(rs.getInt("film_id")),
                mpaStorage.getRatingMpaById(rs.getInt("rating_mpa_id")),
                getFilmGenres(rs.getInt("film_id")),
                directorStorage.getDirectorsByFilmId(rs.getInt("film_id"))), directorId);
    }

    //Удаление фильма по id и возвращение boolean успешности операции
	@Override
	public boolean delete(Integer id) {
		boolean status = false;
		this.checkFilmId(id);
		jdbcTemplate.execute("delete from films where film_id = " + id);
		status = true;
		return status;
	}

	//Удаление всех фильмов
	@Override
	public void clearAll() {
		jdbcTemplate.execute("delete from films");
	}

    // обновление информации в таблице films
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

    // обновление информации в таблице film_genres
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

    // получение жанров фильма из таблицы film_genres по id фильма и полную информацию о них из таблицы genres

    private Set<FilmGenre> getFilmGenres(Integer filmId) {

        SqlRowSet genreRows = jdbcTemplate
                .queryForRowSet("SELECT genre_id FROM film_genres WHERE film_id = ?", filmId);

        Set<FilmGenre> genres = new TreeSet<>(Comparator.comparing(FilmGenre::getId));

        while (genreRows.next()) {
            // получение всей информации о жанре фильма
            FilmGenre filmGenre =
                    filmGenresStorage.getGenreById(genreRows.getInt("genre_id"));
            genres.add(filmGenre);

        }

        return genres;
    }

    // обновление информации в таблице film_directors
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

    // получение информации о рейтинге фильма из таблицы rating_mpa по id фильма
    private Mpa getMpa(Integer filmId) {

        // запрос к таблице films для получения id рейтинга
        SqlRowSet filmMpaRow = jdbcTemplate
                .queryForRowSet("SELECT rating_mpa_id FROM films WHERE film_id = ?", filmId);
        Integer ratingMpaId = null;
        if (filmMpaRow.next()) {
            ratingMpaId = filmMpaRow.getInt("rating_mpa_id");
        }

        // запрос к таблице rating_mpa для получения полной информации о рейтинге
        SqlRowSet mpaRow = jdbcTemplate
                .queryForRowSet("SELECT rating_mpa_name FROM rating_mpa WHERE rating_mpa_id = ? ",
                        ratingMpaId);
        Mpa mpa = null;
        if (mpaRow.next()) {
            // создание объекта рейтинг
            mpa = new Mpa(
                    ratingMpaId,
                    mpaRow.getString("rating_mpa_name"));

        }
        return mpa;
    }

    // получает общие фильмы между двумя пользователями на основе их идентификаторов.
    // Он выполняет SQL-запрос к базе данных, возвращая список фильмов, сгруппированных и отсортированных по количеству лайков
    public List<Film> getCommonFilmsBetweenUsers(Long userId, Long friendId) {
        String sqlFilm = "SELECT f.film_id, f.film_name, f.description, f.release_date, f.duration, r.rating_mpa_id, "
                + "r.rating_mpa_name, COUNT(likes.user_id) AS total_likes "
                + "FROM films AS f "
                + "INNER JOIN rating_mpa AS r ON f.rating_mpa_id = r.rating_mpa_id "
                + "LEFT JOIN likes ON f.film_id = likes.film_id "
                + "INNER JOIN likes AS l1 ON f.film_id = l1.film_id AND l1.user_id = " + userId + " "
                + "INNER JOIN likes AS l2 ON f.film_id = l2.film_id AND l2.user_id = " + friendId + " "
                + "GROUP BY f.film_id, f.film_name, f.description, f.release_date, f.duration, "
                + "r.rating_mpa_id, r.rating_mpa_name "
                + "ORDER BY total_likes DESC";

        List<Film> films = jdbcTemplate.query(sqlFilm, (rs, rowNum) -> new Film(
                rs.getInt("film_id"),
                rs.getString("film_name"),
                rs.getString("description"),
                rs.getDate("release_date").toLocalDate(),
                rs.getInt("duration"),
                rs.getLong("total_likes"),
                new Mpa(rs.getInt("rating_mpa_id"), rs.getString("rating_mpa_name")),
                getFilmGenres(rs.getInt("film_id")),
                directorStorage.getDirectorsByFilmId(rs.getInt("film_id"))));

        return films;
    }


}



