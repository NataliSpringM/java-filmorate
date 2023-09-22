package ru.yandex.practicum.filmorate.storage.db;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;

/**
 * реализация сохранения и получения информации о режиссерах в базе данных
 */
@Slf4j
@Repository
public class DirectorDbStorage implements DirectorStorage {
	private final JdbcTemplate jdbcTemplate;

	public DirectorDbStorage(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Director addDirector(Director director) {
		String sqlQuery = "INSERT INTO directors (director_name) VALUES (?);";
		KeyHolder keyHolder = new GeneratedKeyHolder();

		jdbcTemplate.update(connection -> {
			PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[] { "director_id" });
			stmt.setString(1, director.getName());
			return stmt;
		}, keyHolder);

		return findDirectorById(keyHolder.getKey().intValue());
	}

	@Override
	public Director updateDirector(Director director) {
		int directorId = director.getId();
		String sqlQuery = "UPDATE directors SET director_name = ? WHERE director_id = ?;";
		int result = jdbcTemplate.update(sqlQuery, director.getName(), directorId);
		if (result <= 0) {
			log.info("Не выполнено обновление сведений о режиссере {}", director);
			throw new ObjectNotFoundException(
					String.format("Обновление сведений о режиссере с id=%d не выполнено.", directorId));
		}
		return findDirectorById(directorId);
	}

	@Override
	public void deleteDirector(Integer id) {
		checkDirectorId(id);
		String sqlQuery = "DELETE FROM directors WHERE director_id = ?;";
		jdbcTemplate.update(sqlQuery, id);
	}

	@Override
	public Director findDirectorById(Integer id) {
		String sqlQuery = "SELECT * FROM directors WHERE director_id = ?;";
		SqlRowSet directorRow = jdbcTemplate.queryForRowSet(sqlQuery, id);
		if (!directorRow.next()) {
			log.info("В базе отсутствует режиссер с id={}", id);
			throw new ObjectNotFoundException(String.format("Режиссер с id=%d отсутствует в хранилище.", id));
		}
		Director director = new Director();
		director.setId(directorRow.getInt("director_id"));
		director.setName(directorRow.getString("director_name"));
		return director;
	}

	@Override
	public Collection<Director> findAllDirectors() {
		List<Director> directors = new ArrayList<>();
		String sqlQuery = "SELECT director_id, director_name FROM directors;";

		jdbcTemplate.query(sqlQuery, rs -> {
			Director director = new Director();
			director.setId(rs.getInt("director_id"));
			director.setName(rs.getString("director_name"));
			directors.add(director);
		});
		return directors;
	}

	/**
	 * получение информации о режиссерах фильма по id фильма из таблиц film_director
	 * и directors
	 */
	@Override
	public Set<Director> getDirectorsByFilmId(Integer filmId) {
		/*
		 * SELECT id, director_name FROM (SELECT director_id AS id FROM film_directors
		 * WHERE film_id = ?) LEFT JOIN directors ON id = directors.director_id;
		 */
		SqlRowSet tableRows = jdbcTemplate.queryForRowSet(
				"SELECT id, director_name " + "FROM (SELECT director_id AS id FROM film_directors WHERE film_id = ?) "
						+ "LEFT JOIN directors ON id = directors.director_id;",
				filmId);

		Set<Director> directors = new TreeSet<>(Comparator.comparing(Director::getId));

		while (tableRows.next()) {
			// получение всей информации о режиссерах фильма
			Director director = new Director();
			director.setId(tableRows.getInt("id"));
			director.setName(tableRows.getString("director_name"));
			directors.add(director);
		}
		return directors;
	}

	/**
	 * проверка существования id фильма в базе данных
	 */
	@Override
	public void checkDirectorId(Integer directorId) {
		String sqlQuery = "SELECT director_id FROM directors WHERE director_id = ?;";
		SqlRowSet sqlId = jdbcTemplate.queryForRowSet(sqlQuery, directorId);

		if (!sqlId.next()) {
			log.info("Режиссер с идентификатором {} отсутствует.", directorId);
			throw new ObjectNotFoundException(String.format("Режиссер с id=%d отсутствует в хранилище.", directorId));
		}
	}
}
