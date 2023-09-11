package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.*;
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

    // добавление информации о фильме
    @Override
    public Film addFilm(Film film) {

        // вставляем данные пользователя в базу данных и получаем сгенерированный id
        SimpleJdbcInsert filmInsertion = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("film_id");

        Integer filmId = filmInsertion.executeAndReturnKey(film.toMap()).intValue();

        // присваиваем id и сохраняем полученные данные
        Film filmWithId = film.toBuilder().id(filmId).build();
        updateFilmGenreTable(filmWithId);

        Film newFilm = getFilmById(filmId);
        log.info("Сохранена информация о фильме: {}", newFilm);

        return newFilm;
    }

    // проверка сущестования id фильма в базе данных
    @Override
    public void checkFilmId(Integer filmId) {


        SqlRowSet sqlId = jdbcTemplate.queryForRowSet("select film_id from films where film_id = ?", filmId);

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
                getFilmGenres(rs.getInt("film_id"))));
    }

    // получение информации о фильме по id
    @Override
    public Film getFilmById(Integer filmId) {

        checkFilmId(filmId);

        // получение информации из таблицы films
        SqlRowSet sqlFilm = jdbcTemplate.queryForRowSet("select * from films where film_id = ?", filmId);

        // получение информацию о жанрах фильма из таблицы genres
        Set<FilmGenre> genres = getFilmGenres(filmId);

        // получение информации о рейтинге фильма из таблицы rating_mpa
        Mpa mpa = getMpa(filmId);

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
                    mpa,
                    genres);
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

        // обновление информации  в таблице film_genres о жанрах фильма
        updateFilmGenreTable(film);

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
                getFilmGenres(rs.getInt("film_id"))), limit);

    }

    // обновление информации в таблице films
    private void updateFilmTable(Film film) {

        String sqlQueryFilm = "UPDATE films SET "
                + "film_name =  ?, description = ?, release_date = ?,  duration = ?, rating_mpa_id = ?"
                + "where film_id = ?";

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
            String sqlQueryDel = "delete from film_genres where genre_id = ? and film_id = ?";
            oldGenresId.forEach(genreId -> jdbcTemplate.update(sqlQueryDel, genreId, film.getId()));

            // дополняем данные
            String sqlQueryMerge = "merge into film_genres (film_id, genre_id) values (?, ?)";
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

    // получение информации о рейтинге фильма из таблицы rating_mpa по id фильма
    private Mpa getMpa(Integer filmId) {

        // запрос к таблице films для получения id рейтинга
        SqlRowSet filmMpaRow = jdbcTemplate
                .queryForRowSet("select rating_mpa_id from films where film_id = ?", filmId);
        Integer ratingMpaId = null;
        if (filmMpaRow.next()) {
            ratingMpaId = filmMpaRow.getInt("rating_mpa_id");
        }

        // запрос к таблице rating_mpa для получения полной информации о рейтинге
        SqlRowSet mpaRow = jdbcTemplate
                .queryForRowSet("SELECT rating_mpa_name from rating_mpa where rating_mpa_id = ? ",
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


}



